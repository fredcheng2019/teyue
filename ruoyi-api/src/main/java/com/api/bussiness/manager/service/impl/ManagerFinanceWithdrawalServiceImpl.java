package com.api.bussiness.manager.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.api.base.config.SystemProperties;
import com.api.base.util.CodeUtils;
import com.api.bussiness.manager.service.ManagerFinanceWithdrawalService;
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
import com.api.bussiness.dao.base.BankDao;
import com.api.bussiness.dao.base.WalletKindDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.channel.MpChannelWallet;
import com.api.bussiness.dao.table.merchant.FinancialWallet;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.FinancialWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqFinanceWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspFinanceWithdrawal;


@Service("ManagerFinanceWithdrawal")
@Transactional(rollbackFor = Exception.class)
public class ManagerFinanceWithdrawalServiceImpl implements ManagerFinanceWithdrawalService {
	
	private static Logger log = LoggerFactory.getLogger(ManagerFinanceWithdrawalServiceImpl.class);
	
	@Autowired
	BankDao bankDao;
	
	@Autowired
	MerchantDao merchantDao;
	
	@Autowired
	WalletKindDao walletKindDao;
	
	@Autowired
	ChannelDao channelDao;
	
	@Autowired
	ChannelWalletDao channelWalletDao;
	
	@Autowired
	MerchantWalletDao merchantWalletDao;
	
	@Autowired
    WithdrawalDao withdrawalDao;
	
	@Autowired
	FinancialWalletDao financialWalletDao;

	@Autowired
    SystemProperties systemProperties;
	/**
	 * 平台管理员提现财务钱包
	 */
	@Override
	public ManagerRspFinanceWithdrawal managerFinanceWithdrawal(ManagerReqFinanceWithdrawal drawal) throws Exception{
		ManagerRspFinanceWithdrawal result = new ManagerRspFinanceWithdrawal();
		
		//判断平台所对的钱包是否够钱
		FinancialWallet financialWallet = financialWalletDao.getWalletByChIdAndEnoughMoney(drawal.getChannel_id(), drawal.getTotal_fee());
		//判断渠道钱包是否够钱
		if(null == financialWallet) {
			log.info("自己的钱包不够钱{}",drawal.getChannel_id());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("渠道盈利钱包余额不足");
			return result;
		}
		
		//获取渠道钱包
		MpChannelWallet mpChannelWallet = channelWalletDao.getChannelWalletByChannelIdAndEnoughMoney(drawal.getChannel_id(), drawal.getTotal_fee());
		//判断渠道钱包是否够钱
		if(null == mpChannelWallet) {
			log.info("选择的渠道钱包钱不够{}",drawal.getChannel_id());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("渠道钱包余额不足");
			return result;
		}
		
		//先查询渠道信息，获得代付费用
		MpChannel mpChannel = channelDao.getChannelById(drawal.getChannel_id());
		if(null == mpChannel) {
			log.info("找不到渠道{}",drawal.getChannel_id());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("代付失败");
			return result;
		}
		
		//计算出入帐信息、平台提现自己钱包不用手续费
		BigDecimal centToYuan = new BigDecimal("100");//分转为元
		BigDecimal fee = drawal.getTotal_fee();//提现金额
		//商户出账
		BigDecimal mch_fee = new BigDecimal("0");
		//渠道出账
		BigDecimal ch_fee = fee;
		//盈利
		BigDecimal fin_fee = new BigDecimal("0");
		
		//判断渠道代付规则-内扣-外扣
		if(mpChannel.getWithdrawal_inner() == 0) {
			ch_fee = fee;
		}else if(mpChannel.getWithdrawal_inner() == 1){
			ch_fee = fee.add(mpChannel.getWithdrawal_fee());
		}else {
			log.info("代付渠道内外 扣规则异常,渠道名称：{}",mpChannel.getName());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("代付规则异常");
			return result;
		}
		
		//提现金额不能小于提现手续费
		if(fee.compareTo(mpChannel.getMch_withdrawal_fee()) == -1) {
			log.info("提现金额不能小于提现手续费");
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("提现金额不能小于提现手续费");
			return result;
		}
		
		//判断是否超过最高限额
		if(mch_fee.compareTo(mpChannel.getWithdrawal_limt_right()) == -1 && mch_fee.compareTo(mpChannel.getWithdrawal_limt_left()) == 1) {
			log.info("商户{}提现金额不在范围内",drawal.getChannel_id());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("代付金额须在 "+mpChannel.getWithdrawal_limt_left().divide(centToYuan)+"-"+mpChannel.getWithdrawal_limt_right().divide(centToYuan)+"元内");
			return result;
		}
		
		//冻结平台钱包金额
		int froMchResult = financialWalletDao.frozenBalanceByChId(ch_fee, drawal.getChannel_id());
		if(froMchResult != 1) {
			throw new Exception("平台代付更新平台钱包异常，更新结果："+froMchResult+";渠道id："+drawal.getChannel_id());
		}
		
		//冻结渠道钱包金额
		int froChResult = channelWalletDao.frozenBalanceById(ch_fee, mpChannelWallet.getId());
		if(froChResult != 1) {
			throw new Exception("平台代付更新渠道钱包异常，更新结果："+froChResult+";渠道id："+drawal.getChannel_id());
		}
		
		//生成订单号
		String orderNumber = CodeUtils.createUniqueCode();
		
		//入库
		Withdrawal withDrawInfo = new Withdrawal();
		withDrawInfo.setStatus(1);
		withDrawInfo.setBank_account_no(drawal.getBank_account_no());
		withDrawInfo.setBank_account_owner(drawal.getBank_account_owner());
		withDrawInfo.setBank_account_type(drawal.getBank_account_type());
		withDrawInfo.setBank_branch(drawal.getBank_branch());
		withDrawInfo.setBank_city(drawal.getBank_city());
		withDrawInfo.setBank_code(drawal.getBank_code());
		withDrawInfo.setBank_name(drawal.getBank_name());
		withDrawInfo.setBank_province(drawal.getBank_province());
		withDrawInfo.setBank_sub_branch(drawal.getBank_sub_branch());
		withDrawInfo.setBank_union_no(drawal.getBank_union_no());
		withDrawInfo.setBegin(new Date());
		withDrawInfo.setCallback_url("");//-----------回头配置我们的统一回调地址
		withDrawInfo.setCh_fee(ch_fee);
		withDrawInfo.setChannel_code(mpChannel.getCode());
		withDrawInfo.setChannel_id(mpChannel.getId());
		withDrawInfo.setChannel_mch_withdrawal_fee(mpChannel.getMch_withdrawal_fee());
		withDrawInfo.setChannel_name(mpChannel.getName());
		withDrawInfo.setChannel_class_name(mpChannel.getClass_name());
		withDrawInfo.setChannel_withdrawal_fee(mpChannel.getWithdrawal_fee());
		withDrawInfo.setCode(orderNumber);
		withDrawInfo.setFee(fee);
		withDrawInfo.setSub_fee(fee);
		withDrawInfo.setFin_fee(fin_fee);
		//withDrawInfo.setMch_code(drawal.getOut_trade_no());平台发起没有商户订单号
		withDrawInfo.setMch_fee(mch_fee);
		//withDrawInfo.setMerchant_code(mpMerchant.getCode());
		//withDrawInfo.setMerchant_id(mpMerchant.getId());
		//withDrawInfo.setMerchant_name(mpMerchant.getName());
		withDrawInfo.setMerchant_notify_status(-1);
		//withDrawInfo.setWallet_kind_code(mpWalletKind.getCode());
		//withDrawInfo.setWallet_kind_id(mpWalletKind.getId());
		//withDrawInfo.setWallet_kind_name(mpWalletKind.getName());
		
		int addWithInfoResult = withdrawalDao.addWithdrewInfo(withDrawInfo, "");
		log.info("入库提现信息结果：{}",addWithInfoResult);
		
		//向上游发起代付
		ChannelMethod channelMethod = null;
		try {
			channelMethod = (ChannelMethod)Class.forName("com.sifang.bussiness.channel.type.impl."+mpChannel.getClass_name()).newInstance();
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		if(null == channelMethod) {
			result.setStatus(ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_FAIL);
			result.setMsg("系统异常");
			return result;
		}

		ChannelReqWithdrawal channelReqWithdrawal = new ChannelReqWithdrawal();
		channelReqWithdrawal.setWithdrawal(withDrawInfo);
		channelReqWithdrawal.setMpChannel(mpChannel);
		channelReqWithdrawal.setChannel_notify_url(systemProperties.getNotifyUrl()+"withdrawal/"+mpChannel.getClass_name());

		ChannelRspWithdrawal rsp = getRspWithdrawal(channelMethod,channelReqWithdrawal);
		log.info("财务代付-请求渠道结果:{},订单号:{}",rsp.getStatus(),orderNumber);
		if(rsp.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS) {//代付成功，更新
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 3, rsp.getCh_code());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_SUCCESS);
			result.setMsg("申请成功");
		}else if(rsp.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_FAIL) {
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 1, rsp.getCh_code());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			result.setMsg("申请失败");
		}else if(rsp.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT) {
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 2, rsp.getCh_code());
			result.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			log.info("申请失败-网络不通");
			result.setMsg("申请失败");
		}
		if(rsp.getStatus() != ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS && rsp.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT) {//恢复金额
			int recoverMchResult = financialWalletDao.recoveryBalanceById(fee, drawal.getChannel_id());
			log.info("解冻平台金额情况：{}",recoverMchResult);
			if(recoverMchResult != 1) {
				throw new Exception("平台代付更新平台钱包异常，更新结果："+recoverMchResult+";渠道id："+drawal.getChannel_id());
			}
			int recoverChResult = channelWalletDao.recoveryBalanceById(fee, mpChannelWallet.getId());
			log.info("解冻渠道金额情况：{}",recoverChResult);
			if(recoverChResult != 1) {
				throw new Exception("平台代付更新渠道钱包异常，更新结果："+recoverChResult+";渠道id："+drawal.getChannel_id());
			}
		}
		return result;
	}
	
	
	@HystrixCommand(commandKey = "platformWithdrawalService", 
			groupKey = "platformWithdrawalServiceGroup",
			fallbackMethod = "withDrawalTimeOutFallBack",
			ignoreExceptions = {Exception.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy",value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "10000" )
            })
	public ChannelRspWithdrawal getRspWithdrawal(ChannelMethod channelMethod, ChannelReqWithdrawal req) {
		return channelMethod.withdrawal(req);
	}
	
	public ChannelRspWithdrawal withDrawalTimeOutFallBack(ChannelMethod channelMethod, ChannelReqWithdrawal req) {
		log.info("财务代付超时");
		ChannelRspWithdrawal rsp = new ChannelRspWithdrawal();
		rsp.setStatus(ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT);
		return rsp;
	}
	
}
