package com.api.bussiness.manager.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.api.base.config.SystemProperties;
import com.api.base.util.CodeUtils;
import com.api.bussiness.manager.service.ManagerWithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.bussiness.channel.util.HttpDateUtil;
import com.api.bussiness.dao.area.AreaDao;
import com.api.bussiness.dao.base.BankDao;
import com.api.bussiness.dao.base.WalletKindDao;
import com.api.bussiness.dao.citycode.CityCodeDao;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.table.base.MpBank;
import com.api.bussiness.dao.table.base.MpWalletKind;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.channel.MpChannelWallet;
import com.api.bussiness.dao.table.finance.FinancialSetting;
import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.merchant.MpMerchantWallet;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspWithdrawal;


@Service("managerWithdrawalService")
@Transactional(rollbackFor = Exception.class)
public class ManagerWithdrawalServiceImpl implements ManagerWithdrawalService {
	
	private static Logger log = LoggerFactory.getLogger(ManagerWithdrawalServiceImpl.class);
	
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

	@Override
	public ManagerRspWithdrawal withdrawal(ManagerReqWithdrawal managerReqWithdrawal) throws Exception{		
		
		ManagerRspWithdrawal managerRspWithdrawal = new ManagerRspWithdrawal();
		
		//获得商户信息
		MpMerchant mpMerchant = merchantDao.getMerchantById(managerReqWithdrawal.getMerchant_id());
		if(null == mpMerchant) {
			log.info("商户不存在{}",managerReqWithdrawal.getMerchant_id());;
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("商户不存在");
			return managerRspWithdrawal;
		}
		
		//获取渠道信息
		MpChannel mpChannel = channelDao.getChannelById(managerReqWithdrawal.getChannel_id());
		if(null == mpChannel) {
			log.info("渠道不存在{}",managerReqWithdrawal.getChannel_id());;
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("渠道不存在");
			return managerRspWithdrawal;
		}
		
		//判断商户订单号是否存在
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByMChCodeAndMchantCode(managerReqWithdrawal.getMch_code(), mpMerchant.getCode());
		if(null != withdrawal) {
			log.info("订单号已存在{}",managerReqWithdrawal.getMch_code());
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("商户订单号已存在");
			return managerRspWithdrawal;
		}
		
		//判断渠道订单号是否存在
		int existChCode = withdrawalDao.getWithDrawalByMChCodeAndMchantCodeAndchannelCode(managerReqWithdrawal.getMch_code(), mpMerchant.getCode(),mpChannel.getCode());
		if(existChCode>0) {
			log.info("订单号已存在{}",managerReqWithdrawal.getMch_code());
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("商户订单号已存在");
			return managerRspWithdrawal;
		}
		
		//判断钱包类型是否存在
		MpWalletKind mpWalletKind = walletKindDao.getWalletKindById(managerReqWithdrawal.getWallet_kind_id());
		if(null == mpWalletKind) {
			log.info("钱包类型不存在{}",managerReqWithdrawal.getWallet_kind_id());
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("钱包类型不存在");
			return managerRspWithdrawal;
		}
		
		//提现金额
		BigDecimal total_fee = managerReqWithdrawal.getTotal_fee();
		
		//获得渠道钱包且钱够
		MpChannelWallet mpChannelWallet = channelWalletDao.getChannelWalletByChannelIdAndEnoughMoney(mpChannel.getId(), total_fee);
		
		//判断渠道钱包是否有钱
		if(null == mpChannelWallet) {
			log.info("空支没有满足代付的渠道钱包");;
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("渠道钱包不够钱");
			return managerRspWithdrawal;
		}
		
		//获得商户钱包对应钱包信息
		MpMerchantWallet mpMerchantWalletInfo = merchantWalletDao.getWalletByMerchantIdAndWalletKindId(mpMerchant.getId(), mpWalletKind.getId());
		if(null == mpMerchantWalletInfo) {
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("商户钱包不存在");
			return managerRspWithdrawal;
		}
		
		//判断代付规则
		FinancialSetting financialSetting = financeDao.getFinancialSetting();
		//提交金额
		BigDecimal mch_su_fee = total_fee;
		//实际需要提现金额
		total_fee = total_fee.multiply(mpMerchantWalletInfo.getD_zero());
		//截留金额
		BigDecimal unliquidated_balance = mch_su_fee.subtract(total_fee);
		
		//计算
		//盈利
		BigDecimal fin_fee = mpChannel.getMch_withdrawal_fee().subtract(mpChannel.getWithdrawal_fee());
		//商户实际扣除金额
		BigDecimal mch_real_deduction_amount;
		//商户实际到账金额
		BigDecimal mch_real_arrival_amount;
		//获取平台代付模式-内扣-外口-默认内扣
		if(financialSetting.getWithdrawal_inner() == 0) {//内扣
			mch_real_deduction_amount = total_fee;
			mch_real_arrival_amount = total_fee.subtract(mpChannel.getMch_withdrawal_fee());
		}else if(financialSetting.getWithdrawal_inner() == 1) {//外扣
			mch_real_deduction_amount = total_fee.add(mpChannel.getMch_withdrawal_fee());
			mch_real_arrival_amount = total_fee;
		}else {
			log.info("代付内外 扣规则异常");
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("代付规则异常");
			return managerRspWithdrawal;
		}
		
		//根据商户id和钱包id获取商户钱包，同时要钱够
		MpMerchantWallet mpMerchantWallet = merchantWalletDao.getWalletByMerchantIdAndWalletKindIdAndEnoughMoney(mpMerchant.getId(), mpWalletKind.getId(),mch_real_deduction_amount);
		if(null == mpMerchantWallet) {
			log.info("商户没找到该{}类型的钱包或者钱包不够钱");
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("钱包不存在/余额不足");
			return managerRspWithdrawal;
		}
		
		//渠道实际扣除金额
		BigDecimal ch_real_deduction_amount;
		//提交金额
		BigDecimal sub_fee;
		//渠道代付模式-内扣-外口-默认内扣
		if(mpChannel.getWithdrawal_inner() == 0) {//内扣
			ch_real_deduction_amount = mch_real_arrival_amount.add(mpChannel.getWithdrawal_fee());
			sub_fee = ch_real_deduction_amount;
		}else if(mpChannel.getWithdrawal_inner() == 1){//外扣
			ch_real_deduction_amount = mch_real_arrival_amount.add(mpChannel.getWithdrawal_fee());
			sub_fee = mch_real_arrival_amount;
		}else {
			log.info("代付渠道内外 扣规则异常");
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("代付规则异常");
			return managerRspWithdrawal;
		}
		
		BigDecimal centToYuan = new BigDecimal("100");//分转为元
		
		//代付金额不能小于代付手续费
		if(mch_real_deduction_amount.compareTo(mpChannel.getMch_withdrawal_fee()) == -1) {
			log.info("代付金额不能小于代付手续费");
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("代付金额不能小于代付手续费");
			return managerRspWithdrawal;
		}
		
		//判断是否超过最高限额
		if(mch_real_deduction_amount.compareTo(mpChannel.getWithdrawal_limt_right()) == 1 || mch_real_deduction_amount.compareTo(mpChannel.getWithdrawal_limt_left()) == -1) {
			log.info("代付金额不在范围内 "+mpChannel.getWithdrawal_limt_left().divide(centToYuan)+"-"+mpChannel.getWithdrawal_limt_right().divide(centToYuan)+"元内");
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("代付金额不在范围内");
			return managerRspWithdrawal;
		}
		
		//冻结商户钱包金额
		//int froMchResult = merchantWalletDao.frozenBalanceById(mch_real_deduction_amount, mpMerchantWallet.getId());
		//判断时间范围00:00-11:00闭区间和11:00-00:00开区间
		boolean isEffectiveDate = false;
		try {
			String format = "HH:mm:ss";
			SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
			Date nowTime = new SimpleDateFormat(format).parse(df.format(new Date()));
			
			Date startTime = new SimpleDateFormat(format).parse("00:00:00");
			Date endTime = new SimpleDateFormat(format).parse("11:00:00");
			
			isEffectiveDate = HttpDateUtil.isEffectiveDate(nowTime, startTime, endTime);
		} catch (ParseException e1) {
			log.error("日期出错：{}",e1.getMessage());
			managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_FAIL);;
			managerRspWithdrawal.setMsg("服务器异常");
			return managerRspWithdrawal;
		}
		
		//冻结商户钱包金额
		int froMchResult = 0;
		log.info("时间段:{}",isEffectiveDate);
		if(isEffectiveDate) {//操作unliquidated_balance_one
			froMchResult = merchantWalletDao.frozenBalanceAndAddUnliquidatedBalanceOneById(mch_real_deduction_amount, unliquidated_balance, mpMerchantWallet.getId());
		}else {//操作unliquidated_balance_two
			froMchResult = merchantWalletDao.frozenBalanceAndAddUnliquidatedBalanceTwoById(mch_real_deduction_amount, unliquidated_balance, mpMerchantWallet.getId());
		}
		
		if(froMchResult != 1) {
			throw new Exception("空单代付冻结商户钱包异常，结果："+froMchResult+";商户名称："+mpMerchant.getName()+";钱包名称："+mpWalletKind.getName());
		}
		
		//冻结渠道钱包金额,
		int froChResult = channelWalletDao.frozenBalanceById(ch_real_deduction_amount, mpChannelWallet.getId());
		if(froChResult != 1) {
			throw new Exception("空单代付冻结渠道钱包异常，结果："+froChResult+";渠道名称："+mpChannel.getName());
		}
		
		//生成订单号
		String orderNumber = CodeUtils.createUniqueCode();
		
		//获取银行信息
		MpBank mpBank = bankDao.getBankKindByCode(managerReqWithdrawal.getBank_code());
		if(null == mpBank) {//应该抛异常回滚
			throw new Exception("空单代付没有银行信息，银行代码："+managerReqWithdrawal.getBank_code());
		}
		
		//入库
		Withdrawal withDrawInfo = new Withdrawal();
		withDrawInfo.setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS);
		withDrawInfo.setBank_account_no(managerReqWithdrawal.getBank_account_no());
		withDrawInfo.setBank_account_owner(managerReqWithdrawal.getBank_account_owner());
		withDrawInfo.setBank_account_type(managerReqWithdrawal.getBank_account_type());
		withDrawInfo.setBank_branch(managerReqWithdrawal.getBank_branch());
		withDrawInfo.setBank_city(managerReqWithdrawal.getBank_city());
		withDrawInfo.setBank_code(managerReqWithdrawal.getBank_code());
		withDrawInfo.setBank_name(mpBank.getName());
		withDrawInfo.setBank_province(managerReqWithdrawal.getBank_province());
		withDrawInfo.setBank_sub_branch(managerReqWithdrawal.getBank_sub_branch());
		withDrawInfo.setBank_union_no(managerReqWithdrawal.getBank_union_no());
		withDrawInfo.setBegin(new Date());
		withDrawInfo.setCallback_url("");
		withDrawInfo.setCh_code(managerReqWithdrawal.getCh_code());
		withDrawInfo.setCh_fee(ch_real_deduction_amount);
		withDrawInfo.setChannel_code(mpChannel.getCode());
		withDrawInfo.setChannel_id(mpChannel.getId());
		withDrawInfo.setChannel_mch_withdrawal_fee(mpChannel.getMch_withdrawal_fee());
		withDrawInfo.setChannel_name(mpChannel.getName());
		withDrawInfo.setChannel_class_name(mpChannel.getClass_name());
		withDrawInfo.setChannel_withdrawal_fee(mpChannel.getWithdrawal_fee());
		withDrawInfo.setCode(orderNumber);
		withDrawInfo.setFee(sub_fee);
		withDrawInfo.setSub_fee(sub_fee);
		withDrawInfo.setFin_fee(fin_fee);
		withDrawInfo.setMch_code(managerReqWithdrawal.getMch_code());
		withDrawInfo.setMch_fee(mch_real_deduction_amount);
		withDrawInfo.setMerchant_code(mpMerchant.getCode());
		withDrawInfo.setMerchant_id(mpMerchant.getId());
		withDrawInfo.setMerchant_name(mpMerchant.getName());
		withDrawInfo.setMerchant_notify_status(-1);
		withDrawInfo.setWallet_kind_code(mpWalletKind.getCode());
		withDrawInfo.setWallet_kind_id(mpWalletKind.getId());
		withDrawInfo.setWallet_kind_name(mpWalletKind.getName());

		int addWithInfoResult = withdrawalDao.addWithdrewInfo(withDrawInfo, "");
		log.info("入库代付信息结果：{}",addWithInfoResult);
		if(addWithInfoResult != 1) {
			throw new Exception("空单代付入库订单异常，结果："+addWithInfoResult);
		}
		
		//更新相关钱包
		//更新商户钱包
        int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindId(withDrawInfo.getMch_fee(), withDrawInfo.getMerchant_id(), withDrawInfo.getWallet_kind_id());
        if(resultMch != 1) {
        	throw new Exception("空单更新商户代付商户钱包异常，更新结果："+resultMch+";商户名称："+withDrawInfo.getMerchant_name()+";钱包名称："+withDrawInfo.getWallet_kind_name());
        }

        //更新渠道钱包
        int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindId(withDrawInfo.getCh_fee(), withDrawInfo.getChannel_id());
        if(resultCh != 1) {
        	throw new Exception("空单更新商户代付渠道钱包异常，更新结果："+resultCh+";渠道名称："+withDrawInfo.getChannel_name());
        }
        
        //平台盈利增加
        int resultFin = financeDao.updateFinance(withDrawInfo.getFin_fee(), withDrawInfo.getChannel_id());
        if(resultFin != 1) {
        	throw new Exception("空单更新商户代付平台钱包异常，更新结果："+resultFin+";渠道名称："+withDrawInfo.getChannel_name());
        }
        
		managerRspWithdrawal.setStatus(ManagerRspWithdrawal.MANAGER_RSP_WITHDRAWAL_SUCCESS);;
		managerRspWithdrawal.setMsg("成功");
		return managerRspWithdrawal;
	}

	
	
}
