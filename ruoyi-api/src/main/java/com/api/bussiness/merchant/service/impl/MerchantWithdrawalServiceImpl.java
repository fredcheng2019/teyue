package com.api.bussiness.merchant.service.impl;

import com.api.base.config.SystemProperties;
import com.api.base.util.CodeUtils;
import com.api.base.util.StringUtils;
import com.api.bussiness.dao.area.AreaDao;
import com.api.bussiness.dao.base.BankDao;
import com.api.bussiness.dao.base.WalletKindDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.merchant.bean.MerchantReqWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspWithdrawal;
import com.api.bussiness.merchant.service.MerchantWithdrawalService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.api.bussiness.channel.context.ChannelReqWithdrawal;
import com.api.bussiness.channel.context.ChannelRspWithdrawal;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.channel.util.HttpDateUtil;
import com.api.bussiness.dao.citycode.CityCodeDao;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.table.base.MpBank;
import com.api.bussiness.dao.table.base.MpWalletKind;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.citycode.ERTCityCode;
import com.api.bussiness.dao.table.finance.FinancialSetting;
import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.merchant.MpMerchantMethod;
import com.api.bussiness.dao.table.merchant.MpMerchantWallet;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service("MerchantWithdrawalService")
@Transactional(rollbackFor = Exception.class)//https://www.cnblogs.com/caoyc/p/5632963.html
public class MerchantWithdrawalServiceImpl implements MerchantWithdrawalService {
	
	private static Logger log = LoggerFactory.getLogger(MerchantWithdrawalServiceImpl.class);
	
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
	public MerchantRspWithdrawal withdrawal(MerchantReqWithdrawal merchantReqWithdrawal) throws Exception{

		MerchantRspWithdrawal merchantRspWithdrawal = new MerchantRspWithdrawal();

		//获得商户信息
		MpMerchant mpMerchant = merchantDao.getMerchantByCode(merchantReqWithdrawal.getMch_id());
		if(null == mpMerchant) {
			log.info("商户不存在{}",merchantReqWithdrawal.getMch_id());;

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("商户不存在");

			return merchantRspWithdrawal;
		}
		
		//判断商户订单号是否存在
		Withdrawal withdrawal = withdrawalDao.getWithDrawalByMChCodeAndMchantCode(merchantReqWithdrawal.getOut_trade_no(), merchantReqWithdrawal.getMch_id());
		if(null != withdrawal) {
			log.info("订单号已存在{}",merchantReqWithdrawal.getBank_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("订单号已存在");

			return merchantRspWithdrawal;
		}
		
		//判断钱包类型是否存在
		if(StringUtils.isEmpty(merchantReqWithdrawal.getWallet_type())){
			merchantReqWithdrawal.setWallet_type("shqb");
		}
		MpWalletKind mpWalletKind = walletKindDao.getWalletKindByCode(merchantReqWithdrawal.getWallet_type());
		if(null == mpWalletKind) {
			log.info("钱包类型不存在{}",merchantReqWithdrawal.getWallet_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("钱包类型不存在");

			return merchantRspWithdrawal;
		}

		//提现金额
		BigDecimal total_fee = new BigDecimal(merchantReqWithdrawal.getTotal_fee());

		FinancialSetting financialSetting = financeDao.getFinancialSetting();
		//获得该商户所用到的渠道id
	/*	List<MpMerchantMethod> mpMerchantMethods= merchantDao.getMpMerchantMethodByMerchantCode(mpMerchant.getCode());
		if(null == mpMerchantMethods || mpMerchantMethods.size() == 0) {
			log.info("找不到支付方式{}",merchantReqWithdrawal.getWallet_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("钱包不存在");

			return merchantRspWithdrawal;
		}
		String channelIds = getChannelIds(mpMerchantMethods);

		//判断代付规则

		MpChannelWallet mpChannelWallet = null;
		if(financialSetting.getMch_withdrawal_rule() == 1) {//渠道优先>余额优先
			List<MpChannel> mpChannels = channelDao.getHighestLevelsMpChannelByIds(channelIds);
			if(null != mpChannels && mpChannels.size()>0) {
				int index = 0;
				if(mpChannels.size() > 0) {
					index = NumberUtil.getRandomNumberInRange(0, mpChannels.size()-1);
				}
				mpChannelWallet = channelWalletDao.getChannelWalletByChannelIdAndEnoughMoney(mpChannels.get(index).getId(), total_fee);
			}
		}else if(financialSetting.getMch_withdrawal_rule() == 2) {//余额优先>渠道优先
			List<MpChannelWallet> mpChannelWallets = channelWalletDao.getMostMoneyMpChannelWalletsByChannelIds(channelIds,total_fee);
			if(null != mpChannelWallets && mpChannelWallets.size() > 0) {
				int index = 0;
				if(mpChannelWallets.size() > 0) {
					index = NumberUtil.getRandomNumberInRange(0, mpChannelWallets.size()-1);
				}
				mpChannelWallet = mpChannelWallets.get(index);
			}
		}else {
			log.info("代付规则异常{}",merchantReqWithdrawal.getWallet_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付规则异常");

			return merchantRspWithdrawal;
		}

		//判断渠道钱包是否有钱
		if(null == mpChannelWallet) {
			log.info("没有满足代付的渠道钱包{}",merchantReqWithdrawal.getWallet_type());;

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付失败");

			return merchantRspWithdrawal;
		}*/

		//先查询渠道信息，获得代付费用
		//MpChannel mpChannel = channelDao.getChannelById(withdrawal.getChannel_id());
		MpChannel mpChannel = channelDao.getChannelById(60L);
		if(null == mpChannel) {
			log.info("获得的渠道不可用或不存在，渠道id：{}", 60);;

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付失败");
			
			return merchantRspWithdrawal;
		}
		
		//获得商户钱包对应钱包信息
		MpMerchantWallet mpMerchantWalletInfo = merchantWalletDao.getWalletByMerchantIdAndWalletKindId(mpMerchant.getId(), mpWalletKind.getId());
		if(null == mpMerchantWalletInfo) {
			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("商户钱包不存在");

			return merchantRspWithdrawal;
		}
		
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
			log.info("代付内外 扣规则异常{}",merchantReqWithdrawal.getWallet_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付规则异常");

			return merchantRspWithdrawal;
		}
		
		//根据商户id和钱包id获取商户钱包，同时要钱够
		MpMerchantWallet mpMerchantWallet = merchantWalletDao.getWalletByMerchantIdAndWalletKindIdAndEnoughMoney(mpMerchant.getId(), mpWalletKind.getId(),mch_real_deduction_amount);
		if(null == mpMerchantWallet) {
			log.info("商户没找到该{}类型的钱包或者钱包不够钱",merchantReqWithdrawal.getWallet_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("钱包不存在/余额不足");

			return merchantRspWithdrawal;
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
			log.info("代付渠道内外 扣规则异常{}",merchantReqWithdrawal.getWallet_type());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付规则异常");

			return merchantRspWithdrawal;
		}
		
		BigDecimal centToYuan = new BigDecimal("100");//分转为元
		
		//代付金额不能小于代付手续费
		if(mch_real_deduction_amount.compareTo(mpChannel.getMch_withdrawal_fee()) == -1) {
			log.info("代付金额不能小于代付手续费");

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付金额不能小于代付手续费");
			return merchantRspWithdrawal;
		}
		
		//判断是否超过最高限额
		if(mch_real_deduction_amount.compareTo(mpChannel.getWithdrawal_limt_right()) == 1 || mch_real_deduction_amount.compareTo(mpChannel.getWithdrawal_limt_left()) == -1) {
			log.info("代付金额不在范围内 "+mpChannel.getWithdrawal_limt_left().divide(centToYuan)+"-"+mpChannel.getWithdrawal_limt_right().divide(centToYuan)+"元内");

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("代付金额不在范围内");

			return merchantRspWithdrawal;
		}
		
		//冻结商户钱包金额
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
			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("服务器异常");

			return merchantRspWithdrawal;
		}
		
		//冻结商户钱包金额
		int froMchResult = 0;
		log.info("时间段:{}",isEffectiveDate);
		if(isEffectiveDate) {//操作unliquidated_balance_one
			froMchResult = merchantWalletDao.frozenBalanceAndAddUnliquidatedBalanceOneById(mch_real_deduction_amount, unliquidated_balance, mpMerchantWallet.getId());
		}else {//操作unliquidated_balance_two
			froMchResult = merchantWalletDao.frozenBalanceAndAddUnliquidatedBalanceTwoById(mch_real_deduction_amount, unliquidated_balance, mpMerchantWallet.getId());
		}
		log.info("冻结商户钱包金额结果：{}",froMchResult);
		if(froMchResult != 1) {
			throw new Exception("冻结商户钱包异常：冻结商户的钱包id："+mpMerchantWallet.getId()+";冻结结果："+froMchResult);
		}
		
		//冻结渠道钱包金额
		/*int froChResult = channelWalletDao.frozenBalanceById(ch_real_deduction_amount, mpChannelWallet.getId());
		if(froChResult != 1) {
			throw new Exception("冻结渠道钱包异常：冻结渠道的钱包id："+mpChannelWallet.getId()+";冻结结果："+froChResult);
		}*/
		
		//生成订单号
		String orderNumber = CodeUtils.createUniqueCode();
		
		//获取银行信息
		MpBank mpBank = bankDao.getBankKindByCode(merchantReqWithdrawal.getBank_type());
		if(null == mpBank) {
			log.info("银行信息不存在，银行英文简称：{}",merchantReqWithdrawal.getBank_type());
			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("银行信息不存在");//抛异常回滚

			return merchantRspWithdrawal;
		}
		
		
		//入库
		Withdrawal withDrawInfo = new Withdrawal();
		withDrawInfo.setStatus(Withdrawal.STATUS_ORDER_FAIL);
		withDrawInfo.setBank_account_no(merchantReqWithdrawal.getBank_car_no());
		withDrawInfo.setBank_account_owner(merchantReqWithdrawal.getBank_car_name());
		//withDrawInfo.setBank_account_type(mpMerchantBank.getBank_account_type());
		//判断是对公还是对私的，默认对私
		if(null == merchantReqWithdrawal.getBank_account_type()) {
			withDrawInfo.setBank_account_type(0);
		}else {
			int type = 1;
			try {
				type = Integer.parseInt(merchantReqWithdrawal.getBank_account_type());
				withDrawInfo.setBank_account_type(type);
			}catch (Exception e) {
				type = 0;
			}
			withDrawInfo.setBank_account_type(type);
		}
		
		withDrawInfo.setBank_branch(merchantReqWithdrawal.getBank_address());
		withDrawInfo.setBank_city(merchantReqWithdrawal.getBank_city());
		withDrawInfo.setBank_code(merchantReqWithdrawal.getBank_type());
		withDrawInfo.setBank_name(mpBank.getName());
		withDrawInfo.setBank_province(merchantReqWithdrawal.getBank_province());
		withDrawInfo.setBank_sub_branch(merchantReqWithdrawal.getBranch());
		withDrawInfo.setBank_union_no(merchantReqWithdrawal.getUnionBankNum());
		withDrawInfo.setBegin(new Date());
		withDrawInfo.setCallback_url(merchantReqWithdrawal.getCall_back_url());
		//withDrawInfo.setCh_code();//请求上游后才有 渠道订单号
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
		withDrawInfo.setMch_code(merchantReqWithdrawal.getOut_trade_no());
		withDrawInfo.setMch_fee(mch_real_deduction_amount);
		withDrawInfo.setMerchant_code(mpMerchant.getCode());
		withDrawInfo.setMerchant_id(mpMerchant.getId());
		withDrawInfo.setMerchant_name(mpMerchant.getName());
		withDrawInfo.setMerchant_notify_status(-1);
		withDrawInfo.setWallet_kind_code(mpWalletKind.getCode());
		withDrawInfo.setWallet_kind_id(mpWalletKind.getId());
		withDrawInfo.setWallet_kind_name(mpWalletKind.getName());

		//判断是否需要审核
		if(financialSetting.getWithdrawal_audit() == 0) {//需要审核
			withDrawInfo.setStatus(Withdrawal.STATUS_WITHDRAWAL_APPLY);
			int addWithInfoResult = withdrawalDao.addWithdrewInfo(withDrawInfo, "");
			log.info("入库审核代付信息结果：{}",addWithInfoResult);
			
			merchantRspWithdrawal.setCode("success");
			merchantRspWithdrawal.setMsg("申请成功");
			merchantRspWithdrawal.setMch_id(merchantReqWithdrawal.getMch_id());
			merchantRspWithdrawal.setFbatchno(orderNumber);
			return merchantRspWithdrawal;
		}
		
		int addWithInfoResult = withdrawalDao.addWithdrewInfo(withDrawInfo, "");
		if(addWithInfoResult != 1) {
			throw new Exception("抛出异常");
		}
		
		//向上游发起代付
		ChannelReqWithdrawal channelReqWithdrawal = new ChannelReqWithdrawal();
		channelReqWithdrawal.setWithdrawal(withDrawInfo);
		channelReqWithdrawal.setMpChannel(mpChannel);

		//特殊渠道特殊处理，有些渠道需要城市编码，而且那些编码可能是他们自己定义的
		if(mpChannel.getClass_name().equals("ERTYChannelMethodImpl") || mpChannel.getClass_name().equals("ERTWZChannelMethodImpl")) {
			ERTCityCode eRTCityCode = cityCodeDao.getERTCityCodeByCityNameAndProvinceName(withDrawInfo.getBank_city(), withDrawInfo.getBank_province());
			if(null != eRTCityCode) {
				channelReqWithdrawal.setProvinceCode(eRTCityCode.getProvince_code());
				channelReqWithdrawal.setCityCode(eRTCityCode.getCity_code());
			}else {
				log.info("易付通-找不到城市编码，省：{}，市：{}",withDrawInfo.getBank_province(),withDrawInfo.getBank_city());
				merchantRspWithdrawal.setCode("error");
				merchantRspWithdrawal.setMsg("省或市名称填写错误");
				
				return merchantRspWithdrawal;
			}
		}

		channelReqWithdrawal.setChannel_notify_url(systemProperties.getNotifyUrl()+"withdrawal/"+withDrawInfo.getChannel_class_name());

		ChannelMethod channelMethod = null;
		try {
			channelMethod = (ChannelMethod)Class.forName("com.sifang.bussiness.channel.type.impl."+mpChannel.getClass_name()).newInstance();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if(null == channelMethod) {
			log.info("没有找到代付类：{}",mpChannel.getClass_name());
			int recoverMchResult = merchantWalletDao.recoveryBalanceById(mch_real_deduction_amount, mpMerchantWallet.getId());
			log.info("解冻商户金额情况：{}",recoverMchResult);
		/*	int recoverChResult = channelWalletDao.recoveryBalanceById(ch_real_deduction_amount, mpChannelWallet.getId());
			log.info("解冻渠道金额情况：{}",recoverChResult);*/

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("没有找到代付方法");

			return merchantRspWithdrawal;
		}
		
		ChannelRspWithdrawal channelRspWithdrawal = getChannelRspWithdrawal(channelMethod,channelReqWithdrawal);
		log.info("商户代付-请求渠道结果:{},订单号:{}",channelRspWithdrawal.getStatus(),orderNumber);
		if(channelRspWithdrawal.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS) {//代付成功，更新
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 3, channelRspWithdrawal.getCh_code());

			merchantRspWithdrawal.setCode("success");
			merchantRspWithdrawal.setMsg("申请成功");
			merchantRspWithdrawal.setMch_id(merchantReqWithdrawal.getMch_id());
			merchantRspWithdrawal.setFbatchno(orderNumber);

		}else if(channelRspWithdrawal.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_FAIL) {
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 1, channelRspWithdrawal.getCh_code());

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("申请失败");

		}else if(channelRspWithdrawal.getStatus() == ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT) {
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 2, channelRspWithdrawal.getCh_code());
			log.info("申请失败-超时");

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("申请超时");
		}else {
			withdrawalDao.updateWithDrawalByChannelWithDrawalResult(orderNumber, 1, channelRspWithdrawal.getCh_code());
			log.info("返回异常状态");

			merchantRspWithdrawal.setCode("error");
			merchantRspWithdrawal.setMsg("申请失败");
		}
		if(channelRspWithdrawal.getStatus() != ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS && channelRspWithdrawal.getStatus() != ChannelRspWithdrawal.RSP_STATUS_WITHDRAWAL_TIME_OUT) {//恢复金额
			int recoverMchResult = merchantWalletDao.recoveryBalanceById(mch_real_deduction_amount, mpMerchantWallet.getId());
			log.info("解冻商户金额情况：{}",recoverMchResult);
			/*int recoverChResult = channelWalletDao.recoveryBalanceById(ch_real_deduction_amount, mpChannelWallet.getId());
			log.info("解冻渠道金额情况：{}",recoverChResult);
			if(recoverMchResult != 1 || recoverChResult!= 1) {
				throw new Exception("抛出异常");
			}*/
		}
		return merchantRspWithdrawal;
		
	}
	
	private String getChannelIds(List<MpMerchantMethod> mpMerchantMethods) {
		String ids = "";
		for(int i=0;i<mpMerchantMethods.size();i++) {
			ids = ids + mpMerchantMethods.get(i).getChannel_id() + ",";
		}
		ids = ids.substring(0, ids.length()-1);
		return ids;
	}

	@HystrixCommand(commandKey = "withDrawal001", 
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
