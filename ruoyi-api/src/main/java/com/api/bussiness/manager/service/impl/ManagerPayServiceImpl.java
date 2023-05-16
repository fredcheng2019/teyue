package com.api.bussiness.manager.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.api.base.config.SystemProperties;
import com.api.base.util.CodeUtils;
import com.api.bussiness.manager.service.ManagerPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.merchant.MpMerchantMethod;
import com.api.bussiness.dao.table.order.MpPay;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.manager.bean.ManagerReqPay;
import com.api.bussiness.manager.bean.ManagerRspPay;

@Service("managerPayService")
@Transactional(rollbackFor = Exception.class)
public class ManagerPayServiceImpl implements ManagerPayService {


    private static Logger log = LoggerFactory.getLogger(ManagerPayServiceImpl.class);

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    PayDao payDao;

    @Autowired
    ChannelWalletDao channelWalletDao;

    @Autowired
    MerchantWalletDao merchantWalletDao;

    @Autowired
    FinanceDao financeDao;

    @Autowired
    SystemProperties systemProperties;

    @Override
    public ManagerRspPay pay(ManagerReqPay managerReqPay) throws Exception {

        ManagerRspPay managerRspPay = new ManagerRspPay();

        //获得渠道支付方式
        MpMerchantMethod mpMerchantMethod = merchantDao.getMpMerchantMethodById(managerReqPay.getMp_merchant_method_id());
        if (null == mpMerchantMethod) {
            managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_FAIL);
            managerRspPay.setMsg("支付方式不存在");
            return managerRspPay;
        }

        //判断商户订单号是否存在
        int existMerchantPayCode = payDao.getOrderByMchIdAndMchCodeCount(mpMerchantMethod.getMerchant_code(), managerReqPay.getMch_code());
        if (existMerchantPayCode > 0) {
            managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_FAIL);
            managerRspPay.setMsg("商户订单号已存在");
            return managerRspPay;
        }

        //判断渠道订单号是否存在
        int existChannelPayCode = payDao.getOrderByMchIdAndChCodeAndChannelCodeCount(mpMerchantMethod.getMerchant_code(), mpMerchantMethod.getChannel_code(), managerReqPay.getCh_code());
        if (existChannelPayCode > 0) {
            managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_FAIL);
            managerRspPay.setMsg("渠道订单号已存在");
            return managerRspPay;
        }
        //计算
        BigDecimal divide = new BigDecimal("100");//分转为元
        BigDecimal fee = managerReqPay.getTotal_fee();//交易金额
        BigDecimal mch_fee = fee.subtract(fee.multiply(mpMerchantMethod.getPay_rate()));//商户入账
        BigDecimal ch_fee = fee.subtract(fee.multiply(mpMerchantMethod.getChannel_method_pay_rate()));
        ;//渠道入账
        BigDecimal fin_fee = ch_fee.subtract(mch_fee);//财务入账

        //判断支付限额
        if (fee.compareTo(mpMerchantMethod.getChannel_method_pay_limit_left()) == -1) {//支付金额不能小于最小金额
            log.info("支付金额不能小于" + mpMerchantMethod.getChannel_method_pay_limit_left().divide(divide) + "元");
            managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_FAIL);
            managerRspPay.setMsg("支付金额不能小于" + mpMerchantMethod.getChannel_method_pay_limit_left().divide(divide) + "元");
            return managerRspPay;
        }

        if (fee.compareTo(mpMerchantMethod.getChannel_method_pay_limit_right()) == 1) {//支付金额不能大于最大金额
            log.info("支付金额不能大于" + mpMerchantMethod.getChannel_method_pay_limit_right().divide(divide) + "元");
            managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_FAIL);
            managerRspPay.setMsg("支付金额不能大于" + mpMerchantMethod.getChannel_method_pay_limit_right().divide(divide) + "元");
            return managerRspPay;
        }

        //2生成订单号
        String orderNumber = CodeUtils.createUniqueCode();

        //3入库 订单
        MpPay mpPay = new MpPay();
        Date begin = new Date();
        mpPay.setBegin(begin);
        Date expires = new Date(begin.getTime() + 600000);//加十分钟
        mpPay.setExpires(expires);
        mpPay.setCallback_url("");
        mpPay.setRedirect_url(systemProperties.getRedirectUrl());
        mpPay.setCh_fee(ch_fee);
        mpPay.setChannel_code(mpMerchantMethod.getChannel_code());
        mpPay.setChannel_id(mpMerchantMethod.getChannel_id());
        mpPay.setChannel_method_id(mpMerchantMethod.getChannel_method_id());
        mpPay.setChannel_method_pay_rate(mpMerchantMethod.getChannel_method_pay_rate());
        mpPay.setChannel_name(mpMerchantMethod.getChannel_name());
        mpPay.setCode(orderNumber);
        mpPay.setFee(fee);
        mpPay.setSub_fee(fee);
        mpPay.setFin_fee(fin_fee);
        mpPay.setMch_fee(mch_fee);
        mpPay.setMerchant_code(mpMerchantMethod.getMerchant_code());
        mpPay.setMch_code(managerReqPay.getMch_code());
        mpPay.setCh_code(managerReqPay.getCh_code());
        mpPay.setMerchant_id(mpMerchantMethod.getMerchant_id());
        mpPay.setMerchant_method_id(mpMerchantMethod.getId());
        mpPay.setMerchant_name(mpMerchantMethod.getMerchant_name());
        mpPay.setMerchant_method_pay_rate(mpMerchantMethod.getPay_rate());
        mpPay.setPay_kind_code(mpMerchantMethod.getPay_kind_code());
        mpPay.setPay_kind_id(mpMerchantMethod.getPay_kind_id());
        mpPay.setPay_kind_name(mpMerchantMethod.getPay_kind_name());
        mpPay.setStatus(MpPay.STATUS_PAY_SUCCESS);
        mpPay.setWallet_kind_code(mpMerchantMethod.getWallet_kind_code());
        mpPay.setWallet_kind_id(mpMerchantMethod.getWallet_kind_id());
        mpPay.setWallet_kind_name(mpMerchantMethod.getWallet_kind_name());
        mpPay.setMerchant_notify_status(-1);
        mpPay.setMerchant_notify_status(0);
        mpPay.setChannel_class_name(mpMerchantMethod.getChannel_class_name());
        mpPay.setGoods("");

        payDao.addPay(mpPay, "");

        //更新相关钱包
        //商户钱包加钱
        int resultMch = merchantWalletDao.addMoneyByMchIdAndWalletKindId(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mpPay.getMch_fee());
        if (resultMch != 1) {
            throw new Exception("空单支付更新商户钱包异常，结果：" + resultMch + "商户名称：" + mpPay.getMerchant_name() + ";钱包名称：" + mpPay.getWallet_kind_name());
        }
        //渠道钱包加钱
        int resultCh = channelWalletDao.addMoneyByChannelId(mpPay.getCh_fee(), mpPay.getChannel_id());
        log.info("更新渠道钱包结果:{}", resultCh);
        if (resultCh != 1) {
            throw new Exception("空单支付更新渠道钱包异常，结果：" + resultCh + "渠道名称：" + mpPay.getChannel_name());
        }
        //平台盈利增加
        int resultFin = financeDao.updateFinance(mpPay.getFin_fee(), mpPay.getChannel_id());
        if (resultFin != 1) {
            throw new Exception("空单支付更新平台钱包异常，结果：" + resultFin + "渠道名称：" + mpPay.getChannel_name());
        }
        managerRspPay.setStatus(ManagerRspPay.MANAGER_RSP_PAY_SUCCESS);
        managerRspPay.setMsg("成功");
        return managerRspPay;
    }


}
