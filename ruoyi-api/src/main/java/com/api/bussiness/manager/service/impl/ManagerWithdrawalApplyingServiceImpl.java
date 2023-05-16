package com.api.bussiness.manager.service.impl;

import com.api.base.config.SystemProperties;
import com.api.bussiness.manager.service.ManagerWithdrawalApplyingService;
import com.api.bussiness.dao.table.merchant.MpMerchantWalletFlow;
import com.api.bussiness.dao.wallet.MerchantWalletFlowDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.api.bussiness.channel.context.ChannelReqWithdrawal;
import com.api.bussiness.channel.context.ChannelRspWithdrawal;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.dao.area.AreaDao;
import com.api.bussiness.dao.base.BankDao;
import com.api.bussiness.dao.base.WalletKindDao;
import com.api.bussiness.dao.citycode.CityCodeDao;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.citycode.ERTCityCode;
import com.api.bussiness.dao.table.merchant.MpMerchantWallet;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqWithdrawalApplying;
import com.api.bussiness.manager.bean.ManagerRspWithdrawalApplying;

import java.math.BigDecimal;
import java.util.Date;


@Service("managerWithdrawalApplyingService")
@Transactional(rollbackFor = Exception.class)
public class ManagerWithdrawalApplyingServiceImpl implements ManagerWithdrawalApplyingService {
	
	private static Logger log = LoggerFactory.getLogger(ManagerWithdrawalApplyingServiceImpl.class);
	
	@Autowired
	BankDao bankDao;
	
	@Autowired
	WalletKindDao walletKindDao;
	
	@Autowired
	MerchantWalletDao merchantWalletDao;
	
	@Autowired
	MerchantDao merchantDao;
	
	@Autowired
	ChannelWalletDao channelWalletDao;
	
	@Autowired
	ChannelDao channelDao;
	
	@Autowired
    WithdrawalDao withdrawalDao;
	
	@Autowired
	FinanceDao financeDao;
	
	@Autowired
	AreaDao areaDao;
	
	@Autowired
	CityCodeDao cityCodeDao;
	
	@Autowired
    SystemProperties systemProperties;

	@Autowired
	MerchantWalletFlowDao merchantWalletFlowDao;

	@Override
	public ManagerRspWithdrawalApplying withdrawalPass(ManagerReqWithdrawalApplying managerReqWithdrawalApplying) throws Exception{
		ManagerRspWithdrawalApplying managerRspWithdrawalApplying = new ManagerRspWithdrawalApplying();
		//获得订单
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByCode(managerReqWithdrawalApplying.getCode());
		if(null == withdrawal) {
			managerRspWithdrawalApplying.setMsg("订单不存在");
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			return managerRspWithdrawalApplying;
		}
		
		if(withdrawal.getStatus() != Withdrawal.STATUS_WITHDRAWAL_APPLY) {
			managerRspWithdrawalApplying.setMsg("订单状态不是待审核");
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			return managerRspWithdrawalApplying;
		}
		
		//获得渠道
		MpChannel mpChannel = channelDao.getChannelById(withdrawal.getChannel_id());
		
		//获得商户钱包
		MpMerchantWallet mpMerchantWallet = merchantWalletDao.getWalletByMerchantIdAndWalletKindId(withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
		if(null == mpMerchantWallet) {
			log.info("找不到商户钱包,商户id:{},钱包类型id:{}",withdrawal.getMerchant_id(),withdrawal.getWallet_kind_id());
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			managerRspWithdrawalApplying.setMsg("找不到商户钱包");
			
			return managerRspWithdrawalApplying;
		}
		
		//获得渠道钱包
	/*	MpChannelWallet mpChannelWallet = channelWalletDao.getWalletByChannelId(withdrawal.getChannel_id());
		if(null == mpChannelWallet) {
			log.info("找不到渠道钱包,渠道id:{}",withdrawal.getChannel_id());
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			managerRspWithdrawalApplying.setMsg("找不到渠道钱包");
			
			return managerRspWithdrawalApplying;
		}*/
		
		//向上游发起代付
		ChannelReqWithdrawal channelReqWithdrawal = new ChannelReqWithdrawal();
		channelReqWithdrawal.setWithdrawal(withdrawal);
		channelReqWithdrawal.setMpChannel(mpChannel);

		//特殊渠道特殊处理，有些渠道需要城市编码，而且那些编码可能是他们自己定义的
		if(mpChannel.getClass_name().equals("ERTYChannelMethodImpl") || mpChannel.getClass_name().equals("ERTWZChannelMethodImpl")) {
			ERTCityCode eRTCityCode = cityCodeDao.getERTCityCodeByCityNameAndProvinceName(withdrawal.getBank_city(), withdrawal.getBank_province());
			if(null != eRTCityCode) {
				channelReqWithdrawal.setProvinceCode(eRTCityCode.getProvince_code());
				channelReqWithdrawal.setCityCode(eRTCityCode.getCity_code());
			}else {
				log.info("易付通-找不到城市编码，省：{}，市：{}",withdrawal.getBank_province(),withdrawal.getBank_city());
				managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
				managerRspWithdrawalApplying.setMsg("省或市名称填写错误");
				
				return managerRspWithdrawalApplying;
			}
		}

		channelReqWithdrawal.setChannel_notify_url(systemProperties.getNotifyUrl()+"withdrawal/"+withdrawal.getChannel_class_name());

		ChannelMethod channelMethod = null;
		try {
			channelMethod = (ChannelMethod)Class.forName("com.sifang.bussiness.channel.type.impl."+mpChannel.getClass_name()).newInstance();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		if(null == channelMethod) {
			//更新订单状态
			int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_ORDER_FAIL, "");
			log.info("更新null == channelMethod结果:{}",updateResult);
			log.info("没有找到代付类：{}",mpChannel.getClass_name());
			int recoverMchResult = merchantWalletDao.recoveryBalanceById(withdrawal.getMch_fee(), mpMerchantWallet.getId());
			log.info("解冻商户金额情况：{}",recoverMchResult);
		/*	int recoverChResult = channelWalletDao.recoveryBalanceById(withdrawal.getCh_fee(), mpChannelWallet.getId());
			log.info("解冻渠道金额情况：{}",recoverChResult);*/
			
			managerRspWithdrawalApplying.setMsg("没有找到代付方法");
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			
			return managerRspWithdrawalApplying;
		}

		
	/*	ChannelRspWithdrawal channelRspWithdrawal = getChannelRspWithdrawal(channelMethod,channelReqWithdrawal);
		log.info("商户代付-请求渠道结果:{},订单号:{}",channelRspWithdrawal.getStatus(),withdrawal.getCode());
		if(channelRspWithdrawal.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS) {//代付成功，更新
			int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_FROZING, channelRspWithdrawal.getCh_code());
			log.info("更新代付成功结果:{}",updateResult);
		}else if(channelRspWithdrawal.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_FAIL) {
			int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_ORDER_FAIL, channelRspWithdrawal.getCh_code());
			log.info("更新代付失败结果:{}",updateResult);
		}else if(channelRspWithdrawal.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT) {
			int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_ORDER_TIME_OUT, channelRspWithdrawal.getCh_code());
			log.info("更新代付超时结果:{}",updateResult);
		}else {
			int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_ORDER_FAIL, channelRspWithdrawal.getCh_code());
			log.info("更新代付返回异常结果:{}",updateResult);
			log.info("返回异常状态");
		}
		if(channelRspWithdrawal.getStatus() != ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS && channelRspWithdrawal.getStatus() != ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT) {//恢复金额
			int recoverMchResult = merchantWalletDao.recoveryBalanceById(withdrawal.getMch_fee(), mpMerchantWallet.getId());
			if(recoverMchResult != 1) {
	        	throw new Exception("审核通过更新商户代付商户钱包异常，更新结果："+recoverMchResult+";商户名称："+withdrawal.getMerchant_name()+";钱包名称："+withdrawal.getWallet_kind_name());
	        }
			*//*int recoverChResult = channelWalletDao.recoveryBalanceById(withdrawal.getCh_fee(), mpChannelWallet.getId());
			if(recoverChResult != 1) {
	        	throw new Exception("审核拒绝更新商户代付渠道钱包异常，更新结果："+recoverChResult+";渠道名称："+withdrawal.getChannel_name());
	        }*//*
		}*/

		//不走代付
		int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_FROZING, withdrawal.getCode());
		log.info("更新代付成功结果:{}",updateResult);
		//更新订单状态
		Date date = new Date();
		int result = withdrawalDao.updateWithDrawalStatusByCode(withdrawal.getCode(),withdrawal.getCh_code(), Withdrawal.STATUS_WITHDRAWAL_SUCCESS, date);
		if(result != 1) {
			throw new Exception("更新代付订单异常：更新结果："+result+";订单号："+withdrawal.getCode());
		}
		//判断是商户代付还是渠道代付
		if (null == withdrawal.getMerchant_code() || withdrawal.getMerchant_code().equals("")) {//平台代付
			//更新平台钱包
			int finResult = financeDao.updateFinanceByChannelIdWhenPlatformWithdrewSelf(withdrawal.getCh_fee(), withdrawal.getCh_fee(), withdrawal.getChannel_id());
			if (finResult != 1) {
				throw new Exception("更新平台代付平台钱包异常，更新结果：" + finResult + ";渠道名称：" + withdrawal.getChannel_name());
			}
		}else {//商户代付
			//增加商户流水
			 addMerchantWalletFlow(withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id(), withdrawal.getMch_fee().negate(), withdrawal.getCode());
			//更新商户钱包
			int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindId(withdrawal.getMch_fee(), withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
			if (resultMch != 1) {
				throw new Exception("更新商户代付商户钱包异常，更新结果：" + resultMch + ";商户名称：" + withdrawal.getMerchant_name() + ";钱包名称：" + withdrawal.getWallet_kind_name());
			}
			//平台盈利增加
			BigDecimal fin_fee = withdrawal.getFin_fee();
			int resultFin = financeDao.updateFinance(fin_fee, withdrawal.getChannel_id());
			if(resultFin != 1) {
				throw new Exception("更新商户代付平台钱包异常，更新结果："+resultFin+";渠道名称："+withdrawal.getChannel_name());
			}
		}
		
		managerRspWithdrawalApplying.setMsg("操作成功");
		managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_SUCCESS);
		
		return managerRspWithdrawalApplying;
	}

	private void addMerchantWalletFlow( long merchant_id, long wallet_kind_id, BigDecimal fee , String order_id){
		MpMerchantWallet mpMerchantWallet = merchantWalletDao.getWalletByMerchantIdAndWalletKindId(merchant_id, wallet_kind_id);
		mpMerchantWallet = merchantWalletDao.getWalletByIdForUpdate(mpMerchantWallet.getId());
		MpMerchantWalletFlow flow = new MpMerchantWalletFlow();
		flow.setFlow_type(MpMerchantWalletFlow.FLOW_TYPE2);
		flow.setMerchant_id(Long.valueOf(mpMerchantWallet.getMerchant_id()));
		flow.setMerchant_name(merchantDao.getMerchantById(mpMerchantWallet.getMerchant_id()).getName()+"");
		flow.setPrevious_balance(mpMerchantWallet.getAccount_balance());
		flow.setAmount(fee);
		BigDecimal amount = mpMerchantWallet.getAccount_balance().add(fee);
		flow.setCurrent_balance(amount);
		flow.setCreated_time(new Date());
		flow.setCreated_by("系统");
		flow.setFlow_direction(fee.intValue() > 0 ? MpMerchantWalletFlow.FLOW_DIRECTION1 : MpMerchantWalletFlow.FLOW_DIRECTION2);
		flow.setFlow_detail_type( MpMerchantWalletFlow.FLOW_DETAIL_TYPE2 );
		flow.setOrder_id(order_id);
		merchantWalletFlowDao.addMerchantWalletFlow(flow, "");
	}

	@Override
	public ManagerRspWithdrawalApplying withdrawalRefuse(ManagerReqWithdrawalApplying managerReqWithdrawalApplying) throws Exception{
		
		ManagerRspWithdrawalApplying managerRspWithdrawalApplying = new ManagerRspWithdrawalApplying();
		//获得订单
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByCode(managerReqWithdrawalApplying.getCode());
		if(null == withdrawal) {
			managerRspWithdrawalApplying.setMsg("订单不存在");
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			return managerRspWithdrawalApplying;
		}
		
		if(withdrawal.getStatus() != Withdrawal.STATUS_WITHDRAWAL_APPLY) {
			managerRspWithdrawalApplying.setMsg("订单状态不是待审核");
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			return managerRspWithdrawalApplying;
		}
		
		//获得商户钱包
		MpMerchantWallet mpMerchantWallet = merchantWalletDao.getWalletByMerchantIdAndWalletKindId(withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
		if(null == mpMerchantWallet) {
			log.info("找不到商户钱包,商户id:{},钱包类型id:{}",withdrawal.getMerchant_id(),withdrawal.getWallet_kind_id());
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			managerRspWithdrawalApplying.setMsg("找不到商户钱包");
			return managerRspWithdrawalApplying;
		}
		
		//获得渠道钱包
	/*	MpChannelWallet mpChannelWallet = channelWalletDao.getWalletByChannelId(withdrawal.getChannel_id());
		if(null == mpChannelWallet) {
			log.info("找不到渠道钱包,渠道id:{}",withdrawal.getChannel_id());
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			managerRspWithdrawalApplying.setMsg("找不到渠道钱包");
			return managerRspWithdrawalApplying;
		}*/
		
		
		//更新订单状态
		int updateResult = withdrawalDao.updateWithDrawalByChannelWithDrawalResult(withdrawal.getCode(), Withdrawal.STATUS_ORDER_FAIL, "");
		if(updateResult != 1) {
        	throw new Exception("审核拒绝更新代付订单异常：更新结果："+updateResult+";订单号："+withdrawal.getCode());
        }
		int recoverMchResult = merchantWalletDao.recoveryBalanceById(withdrawal.getMch_fee(), mpMerchantWallet.getId());
		if(recoverMchResult != 1) {
        	throw new Exception("审核拒绝更新商户代付商户钱包异常，更新结果："+recoverMchResult+";商户名称："+withdrawal.getMerchant_name()+";钱包名称："+withdrawal.getWallet_kind_name());
        }
		/*int recoverChResult = channelWalletDao.recoveryBalanceById(withdrawal.getCh_fee(), mpChannelWallet.getId());
		if(recoverChResult != 1) {
        	throw new Exception("审核拒绝更新商户代付渠道钱包异常，更新结果："+recoverChResult+";渠道名称："+withdrawal.getChannel_name());
        }*/
		
		managerRspWithdrawalApplying.setMsg("操作成功");
		managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_SUCCESS);
		
		return managerRspWithdrawalApplying;
	}
	
	@HystrixCommand(commandKey = "withDrawal101", 
			groupKey = "withDrawalGroup",
			fallbackMethod = "getChannelRspWithdrawalFallBack",
			ignoreExceptions = {Exception.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy",value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "10000" )
            })
	public ChannelRspWithdrawal getChannelRspWithdrawal(ChannelMethod channelMethod, ChannelReqWithdrawal req) {
		return channelMethod.withdrawal(req);
	}
	
	public ChannelRspWithdrawal getChannelRspWithdrawalFallBack(ChannelMethod channelMethod, ChannelReqWithdrawal req) {
		log.info("商户代付超时");
		ChannelRspWithdrawal rsp = new ChannelRspWithdrawal();
		rsp.setStatus(ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT);
		return rsp;
	}

}
