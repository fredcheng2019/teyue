package com.api.bussiness.merchant.service.impl;

import com.api.base.config.SystemProperties;
import com.api.base.util.CodeUtils;
import com.api.base.util.NumberUtil;
import com.api.base.util.StringUtils;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.merchant.bean.MerchantReqPay;
import com.api.bussiness.merchant.bean.MerchantRspPay;
import com.api.bussiness.merchant.service.MerchantPayService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.api.bussiness.channel.context.ChannelReqPay;
import com.api.bussiness.channel.context.ChannelRspPay;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.merchant.MpMerchantMethod;
import com.api.bussiness.dao.table.order.MpPay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service("MerchantPayService")
public class MerchantPayServiceImpl implements MerchantPayService {

    private static Logger log = LoggerFactory.getLogger(MerchantPayServiceImpl.class);
    private static BigDecimal usdtRate = null;
    @Autowired
    MerchantDao merchantDao;
    @Autowired
    PayDao payDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    SystemProperties systemProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantRspPay pay(MerchantReqPay merchantReqPay) throws Exception {
        MerchantRspPay merchantRsqPay = new MerchantRspPay();
        //获取商户信息
        MpMerchant mpMerchant = merchantDao.getMerchantByCode(merchantReqPay.getMch_id());
        //判断商户是否正常
        if (null == mpMerchant) {
            log.info("商户{}不存在。", merchantReqPay.getMch_id());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("商户不存在");
            return merchantRsqPay;
        } else if (mpMerchant.getStatus() != 0) {
            log.info("商户{}已停用。", merchantReqPay.getMch_id());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("商户已停用");
            return merchantRsqPay;
        }
        //判断订单号是否存在
        int existMerchantPayCode = payDao.getOrderByMchIdAndMchCodeCount(merchantReqPay.getMch_id(), merchantReqPay.getOut_trade_no());
        if (existMerchantPayCode > 0) {
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("订单已存在");
            return merchantRsqPay;
        }
        //交易金额
        BigDecimal fee = new BigDecimal(merchantReqPay.getTotal_fee());
        //判断最高级别是否有 多个 ，若有多个，随机拿一个 通知判断有效性
        //List<MpMerchantMethod> mpMerchantMethods = merchantDao.getHighestLevelsByMchIdAndPayKindCode(mpMerchant.getId(), merchantReqPay.getService());
        List<MpMerchantMethod> mpMerchantMethods = merchantDao.getHighestLevelsByMchIdAndPayKindCodeAndFee(mpMerchant.getId(), merchantReqPay.getService(), fee);
        MpMerchantMethod mpMerchantMethod = null;
        if (null == mpMerchantMethods || mpMerchantMethods.size() == 0) {
            log.info("支付方法不存在,商户号{},支付方式：{}", mpMerchant.getId(), merchantReqPay.getService());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("支付方法不存在");
            return merchantRsqPay;
        } else {
            if (mpMerchantMethods.size() == 1) {
                mpMerchantMethod = mpMerchantMethods.get(0);
            } else {
                int index = NumberUtil.getRandomNumberInRange(0, mpMerchantMethods.size() - 1);
                mpMerchantMethod = mpMerchantMethods.get(index);
            }
        }
        String channelMethodCode = channelDao.getChannelMethodById(mpMerchantMethod.getChannel_method_id());
        if (channelMethodCode == null) {
            log.info("支付方法为配置渠道编码,商户号{},支付方式：{}", mpMerchant.getId(), merchantReqPay.getService());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("支付方法未配置渠道编码");
            return merchantRsqPay;
        }
        BigDecimal agentRate = new BigDecimal(0);
        //这里表示商户绑定了代理商,所以需要根据代理商来计算
        if (mpMerchant.getAgent_id() != null && mpMerchant.getAgent_id().intValue() != 0) {
            MpMerchant agentMerchant = merchantDao.getMerchantByUserId(mpMerchant.getAgent_id());
            if(agentMerchant.getAgent_profit() != null){
                agentRate = agentMerchant.getAgent_profit();
            }
        }

        //计算
        BigDecimal divide = new BigDecimal("100");//分转为元

        //渠道入账
        BigDecimal ch_fee = fee.subtract(fee.multiply(mpMerchantMethod.getChannel_method_pay_rate()));
        //商户入账
        BigDecimal mch_fee = fee.subtract(fee.multiply(mpMerchantMethod.getPay_rate()));
        //代理入账
        BigDecimal agent_fee = fee.multiply(agentRate);
        //财务入账 (最终财务入账 = (渠道入账- 商户入账-代理入账))
        BigDecimal fin_fee = ch_fee.subtract(mch_fee).subtract(agent_fee);
        //判断支付限额
        if (fee.compareTo(mpMerchantMethod.getChannel_method_pay_limit_left()) == -1) {//支付金额不能小于最小金额
            log.info("支付金额不能小于" + mpMerchantMethod.getChannel_method_pay_limit_left().divide(divide) + "元");
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("支付金额不能小于" + mpMerchantMethod.getChannel_method_pay_limit_left().divide(divide) + "元");
            return merchantRsqPay;
        }

        if (fee.compareTo(mpMerchantMethod.getChannel_method_pay_limit_right()) == 1) {//支付金额不能大于最大金额
            log.info("支付金额不能大于" + mpMerchantMethod.getChannel_method_pay_limit_right().divide(divide) + "元");
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("支付金额不能大于" + mpMerchantMethod.getChannel_method_pay_limit_right().divide(divide) + "元");
            return merchantRsqPay;
        }

        //2生成订单号
        String orderNumber = CodeUtils.createUniqueCode();

        //3入库 订单
        MpPay mpPay = new MpPay();
        Date begin = new Date();
        mpPay.setBegin(begin);
        Date expires = new Date(begin.getTime() + 600000);//加十分钟
        mpPay.setExpires(expires);
        mpPay.setCallback_url(merchantReqPay.getNotify_url());
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
        mpPay.setMerchant_code(mpMerchant.getCode());
        mpPay.setMch_code(merchantReqPay.getOut_trade_no());
        mpPay.setMerchant_id(mpMerchant.getId());
        mpPay.setMerchant_method_id(mpMerchantMethod.getId());
        mpPay.setMerchant_name(mpMerchant.getName());
        mpPay.setMerchant_method_pay_rate(mpMerchantMethod.getPay_rate());
        mpPay.setPay_kind_code(mpMerchantMethod.getPay_kind_code());
        mpPay.setPay_kind_id(mpMerchantMethod.getPay_kind_id());
        mpPay.setPay_kind_name(mpMerchantMethod.getPay_kind_name());
        mpPay.setStatus(1);
        mpPay.setWallet_kind_code(mpMerchantMethod.getWallet_kind_code());
        mpPay.setWallet_kind_id(mpMerchantMethod.getWallet_kind_id());
        mpPay.setWallet_kind_name(mpMerchantMethod.getWallet_kind_name());
        mpPay.setMerchant_notify_status(-1);
        mpPay.setMerchant_notify_status(0);
        mpPay.setChannel_class_name(mpMerchantMethod.getChannel_class_name());
        mpPay.setGoods(merchantReqPay.getBody());
        mpPay.setAgent_fee(agent_fee);
        mpPay.setAgent_id(mpMerchant.getAgent_id());
        mpPay.setAgent_pay_rate(agentRate);
        mpPay.setClient_ip(merchantReqPay.getClientIP());
        mpPay.setRemarks(merchantReqPay.getRemark());
        mpPay.setUsdt_rate(getUsdtRate());

        int addPayResult = payDao.addPay(mpPay, "");
        log.info("入库订单状态：{}，订单号：{}", addPayResult, orderNumber);
        ChannelMethod channelMethod = null;
        try {
            channelMethod = (ChannelMethod) Class.forName("com.sifang.bussiness.channel.type.impl." + mpMerchantMethod.getChannel_class_name()).newInstance();
        } catch (Exception e) {
            log.info("加载支付方法异常");
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("加载支付方法异常");
            return merchantRsqPay;
        }

        //向上游下单
        ChannelReqPay channelReqPay = new ChannelReqPay();
        channelReqPay.setMpPay(mpPay);
        MpChannel mpChannel = new MpChannel();
        mpChannel.setCode(mpMerchantMethod.getChannel_code());
        mpChannel.setReq_url(mpMerchantMethod.getChannel_req_url());
        mpChannel.setSecret_key(mpMerchantMethod.getChannel_secret_key());
        mpChannel.setChannelMethodCode(channelMethodCode);
        channelReqPay.setMpChannel(mpChannel);
        channelReqPay.setChannel_redirect_url(systemProperties.getRedirectUrl());
        channelReqPay.setChannel_notify_url(systemProperties.getNotifyUrl() + "pay/" + mpPay.getChannel_class_name());
        channelReqPay.setJump_pay_url(systemProperties.getJumpPayUrl());

        ChannelRspPay channelRspPay = getChannelRspPay(channelMethod, channelReqPay);
        //根据上游下单情况更新订单状态
        if (channelRspPay.getStatus() == ChannelRspPay.RSP_STATUS_PAY_SUCCESS) {//下单成功，更新订单
            payDao.updatePayByChannelPayResult(orderNumber, MpPay.STATUS_WAIT_TO_PAY, channelRspPay.getCh_code());
            merchantRsqPay.setCode("success");
            merchantRsqPay.setMsg("下单成功");
            merchantRsqPay.setCode_url(channelRspPay.getCode_url());
            merchantRsqPay.setMch_id(merchantReqPay.getMch_id());
            merchantRsqPay.setOrder_sn(orderNumber);
            merchantRsqPay.setOut_trade_no(merchantReqPay.getOut_trade_no());
        } else if (channelRspPay.getStatus() == ChannelRspPay.RSP_STATUS_PAY_FAIL) {
            payDao.updatePayByChannelPayResult(orderNumber, MpPay.STATUS_ORDER_FAIL, channelRspPay.getCh_code());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg(StringUtils.isBlank(channelRspPay.getMsg()) ? "下单失败" : channelRspPay.getMsg());
        } else if (channelRspPay.getStatus() == ChannelRspPay.RSP_STATUS_PAY_TIME_OUT) {
            payDao.updatePayByChannelPayResult(orderNumber, MpPay.STATUS_ORDER_TIME_OUT, channelRspPay.getCh_code());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("下单超时");
        } else {
            payDao.updatePayByChannelPayResult(orderNumber, MpPay.STATUS_ORDER_FAIL, channelRspPay.getCh_code());
            merchantRsqPay.setCode("error");
            merchantRsqPay.setMsg("未知");
        }
        return merchantRsqPay;
    }

    @HystrixCommand(commandKey = "pay001", groupKey = "payGroup", fallbackMethod = "getChannelRspPayTimeOutFallBack", ignoreExceptions = {Exception.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            })
    public ChannelRspPay getChannelRspPay(ChannelMethod channelMethod, ChannelReqPay req) {
        return channelMethod.pay(req);
    }

    public ChannelRspPay getChannelRspPayTimeOutFallBack(ChannelMethod channelMethod, ChannelReqPay req) {
        ChannelRspPay order = new ChannelRspPay();
        order.setStatus(ChannelRspPay.RSP_STATUS_PAY_TIME_OUT);
        return order;
    }

    public BigDecimal getUsdtRate() {
        try {
            BigDecimal temp = payDao.getUsdtRate();
            if (temp == null) {
                usdtRate = BigDecimal.valueOf(0);
            } else {
                usdtRate = temp;
            }
        } catch (Exception e) {
            log.error("查询usdt费率出现问题--------------------------------");
            usdtRate = BigDecimal.ZERO;
        }
        return usdtRate;
    }

}
