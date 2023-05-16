package com.api.bussiness.manager.service.impl;

import com.api.base.util.MchHttpUtil;
import com.api.bussiness.manager.service.ManagerNotifyPayService;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.merchant.MpMerchantWallet;
import com.api.bussiness.dao.table.merchant.MpMerchantWalletFlow;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletFlowDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.order.MpPay;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqNotifyPay;
import com.api.bussiness.manager.bean.ManagerRspNotifyPay;
import com.api.bussiness.merchant.service.MerchantNotifyService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;


@Service("ManagerNotifyPayService")
public class ManagerNotifyPayServiceImpl implements ManagerNotifyPayService {

	private static Logger log = LoggerFactory.getLogger(ManagerNotifyPayServiceImpl.class);
	@Autowired
	PayDao payDao;

	@Autowired
	WithdrawalDao withdrawalDao;

	@Autowired
	ChannelWalletDao channelWalletDao;

	@Autowired
	MerchantWalletDao merchantWalletDao;

	@Autowired
	MerchantNotifyService merchantNotifyService;

	@Autowired
	FinanceDao financeDao;

	@Autowired
	MerchantDao merchantDao;

	@Autowired
	MerchantWalletFlowDao merchantWalletFlowDao;

	@Override
	@Transactional
	public ManagerRspNotifyPay notifyMerchantPay(ManagerReqNotifyPay managerReqNotifyPay) {
		log.info("通知商户：{}", managerReqNotifyPay.getCode());
		ManagerRspNotifyPay managerRspNotifyPay = new ManagerRspNotifyPay();
		MpPay mpPay = payDao.getOrderByCode(managerReqNotifyPay.getCode());
		if(mpPay.getStatus() != MpPay.STATUS_PAY_SUCCESS && managerReqNotifyPay.getSetPaySuccess()==1){
			Date end= new Date();
			int updateResult = payDao.updatePayStatusAndEndById(mpPay.getId(),MpPay.STATUS_PAY_SUCCESS,end);
			if (updateResult != 1) {
				managerRspNotifyPay.setMsg("已通知");
				managerRspNotifyPay.setStatus(ManagerRspNotifyPay.RSP_STATUS_SUCCESS);
				return managerRspNotifyPay;
			}
			mpPay.setStatus(MpPay.STATUS_PAY_SUCCESS);
			mpPay.setEnd(end);
			// 补回平台利润? todo
			//加钱,根据商户信息是否为空来判断是商户还是平台
			if (null == mpPay.getMerchant_code() || mpPay.getMerchant_code().equals("")) {
				//渠道钱包加钱
				BigDecimal ch_fee = mpPay.getCh_fee();
				channelWalletDao.addMoneyByChannelId(ch_fee, mpPay.getChannel_id());
			} else {
				//商户支付 //商户钱包加钱
				BigDecimal mch_fee = mpPay.getMch_fee();
				//增加商户流水
				addMerchantWalletFlow(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mch_fee, mpPay.getCode());
				merchantWalletDao.addMoneyByMchIdAndWalletKindId(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mch_fee);
				//渠道钱包加钱
				BigDecimal ch_fee = mpPay.getCh_fee();
				channelWalletDao.addMoneyByChannelId(ch_fee, mpPay.getChannel_id());
				//如果订单有代理
				if (mpPay.getAgent_id() != null && mpPay.getAgent_id().intValue() != 0) {
					//获取代理信息
					MpMerchant agentMerchant = merchantDao.getMerchantByUserId(mpPay.getAgent_id());
					// 代理钱包加钱
					BigDecimal agent_fee = mpPay.getAgent_fee();
					merchantWalletDao.addMoneyByMchIdAndWalletKindId(agentMerchant.getId(), mpPay.getWallet_kind_id(), agent_fee);
				}
			}
			//平台盈利增加
			BigDecimal fin_fee = mpPay.getFin_fee();
			financeDao.updateFinance(fin_fee, mpPay.getChannel_id());
		}
		//同步通知商户
		int status = merchantNotifyService.payNotify(mpPay);
		//通知次数加一
		payDao.updateAddMerchantNotifyTimesByCode(mpPay.getCode());
		if(status == MchHttpUtil.SEND_STATUS_SUCCESS) {
			managerRspNotifyPay.setMsg("已通知");
			managerRspNotifyPay.setStatus(ManagerRspNotifyPay.RSP_STATUS_SUCCESS);
			return managerRspNotifyPay;
		}else {
			managerRspNotifyPay.setMsg("通知失败");
			managerRspNotifyPay.setStatus(ManagerRspNotifyPay.RSP_STATUS_FAIL);
			return managerRspNotifyPay;
		}
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
		flow.setFlow_detail_type( MpMerchantWalletFlow.FLOW_DETAIL_TYPE1 );
		flow.setOrder_id(order_id);
		merchantWalletFlowDao.addMerchantWalletFlow(flow, "");
	}

}
