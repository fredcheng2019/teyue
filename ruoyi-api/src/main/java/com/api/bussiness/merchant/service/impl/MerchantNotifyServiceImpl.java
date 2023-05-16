package com.api.bussiness.merchant.service.impl;

import com.api.base.util.MchHttpUtil;
import com.api.base.util.StringUtils;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.merchant.bean.MerchantReqNotify;
import com.api.bussiness.merchant.service.MerchantNotifyService;
import com.api.bussiness.merchant.service.TelegramNotifyService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.order.MpPay;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;


@Service("MerchantNotifyService")
public class MerchantNotifyServiceImpl implements MerchantNotifyService {

    private static Logger log = LoggerFactory.getLogger(MerchantNotifyServiceImpl.class);

    @Autowired
    PayDao payDao;

    @Autowired
    WithdrawalDao withdrawalDao;

    @Autowired
    MerchantDao merchantDao;

    @Value("${map.tele.url}")
    public String teleurl;

    @Autowired
    TelegramNotifyService telegramNotifyService;


    @Async
    @Override
    public void payNotifySync(MpPay pay) {
        try {
            Thread.sleep(1000);// 加处理时间差
        } catch (InterruptedException e) {
        }
        if (pay.getStatus() < MpPay.STATUS_PAY_FAIL) {
            log.error("异步 通知订单不存在或订单状态未更新。平台订单号:{}，订单状态：{}", pay.getCode(), pay.getStatus());
            log.info("异步 通知订单不存在或订单状态未更新。平台订单号:{}，订单状态：{}", pay.getCode(), pay.getStatus());
            return;
        }

        MpMerchant mpMerchant = merchantDao.getMerchantByCode(pay.getMerchant_code());
        //根据订单信息通知商户
        if (null != pay.getCallback_url() && !pay.getCallback_url().equals("")) {
            //封装报文通知商户
            MerchantReqNotify merchantReqNotify = new MerchantReqNotify();
            merchantReqNotify.setBody(pay.getGoods());
            merchantReqNotify.setFinish_time(pay.getEnd().getTime() + "");
            merchantReqNotify.setMch_id(pay.getMerchant_code());
            merchantReqNotify.setOrder_date(pay.getBegin().getTime() + "");
            merchantReqNotify.setOrder_sn(pay.getCode());
            merchantReqNotify.setOut_trade_no(pay.getMch_code());
            merchantReqNotify.setService(pay.getPay_kind_code());
            merchantReqNotify.setRemark(pay.getRemarks());
            if (pay.getStatus() == MpPay.STATUS_PAY_SUCCESS) {
                merchantReqNotify.setStatus(1);
            } else {
                merchantReqNotify.setStatus(0);
            }
            merchantReqNotify.setTime_expire(pay.getExpires().getTime() + "");
            merchantReqNotify.setTime_start(pay.getBegin().getTime() + "");
            merchantReqNotify.setTotal_fee(pay.getFee());
            int status = getSendMchCallbackStatus(merchantReqNotify, mpMerchant.getSecret_key(), pay.getCallback_url());
            log.info("通知商户状态#:{}", status);
            //更新通知状态
            if (status == MchHttpUtil.SEND_STATUS_SUCCESS) {
                int result = payDao.updatePayMerchantNotifyStatusByCode(pay.getCode(), 1);
                log.info("1更新通知状态结果：{}", result);
            } else if (status == MchHttpUtil.SEND_STATUS_FAIL) {
                int result = payDao.updatePayMerchantNotifyStatusByCode(pay.getCode(), 0);
                log.info("2更新通知状态结果：{}", result);
            } else if (status == MchHttpUtil.SEND_STATUS_TIME_OUT) {
                int result = payDao.updatePayMerchantNotifyStatusByCode(pay.getCode(), 0);
                log.info("3更新通知状态结果：{}", result);
            }
        }

    }

    @Override
    public int payNotify(MpPay pay) {
        if (pay.getStatus() < MpPay.STATUS_PAY_FAIL) {
            log.error("异步 通知订单不存在或订单状态未更新。平台订单号:{}，订单状态：{}", pay.getCode(), pay.getStatus());
            log.info("异步 通知订单不存在或订单状态未更新。平台订单号:{}，订单状态：{}", pay.getCode(), pay.getStatus());
            return MchHttpUtil.SEND_STATUS_FAIL;
        }
        MpMerchant mpMerchant = merchantDao.getMerchantByCode(pay.getMerchant_code());


        //根据订单信息通知商户
        if (null != pay.getCallback_url() && !pay.getCallback_url().equals("")) {
            //封装报文通知商户
            MerchantReqNotify merchantReqNotify = new MerchantReqNotify();
            merchantReqNotify.setBody(pay.getGoods());
            if (pay.getEnd() == null) {
                log.info("用户未支付");
                return MchHttpUtil.SEND_STATUS_FAIL;
            }
           /* mch_id	商户号	必传	平台下发商户号
            out_trade_no	商户订单号	必传	商户订单号
            order_sn	平台订单号	必传	平台订单号
            total_fee	订单金额	必传	订单金额以分为单位
            service	支付类型编码	必传	支付类型编码如：wxqr
            body	商品描述	必传	下单时传的信息
            time_start	订单开始时间	必传	时间戳
            time_expire	订单过期时间	必传	时间戳
            finish_time	支付时间	必传	时间戳
            status	状态	必传	1为已支付0为未支付
            order_date	订单时间	必传	订单时间
            remark	备注	非必传	备注
            sign	验签字段	必传	签名*/
            merchantReqNotify.setOut_trade_no(pay.getMch_code());
            merchantReqNotify.setFinish_time(pay.getEnd().getTime() + "");
            merchantReqNotify.setMch_id(pay.getMerchant_code());
            merchantReqNotify.setOrder_date(pay.getBegin().getTime() + "");
            merchantReqNotify.setOrder_sn(pay.getCode());
            merchantReqNotify.setOut_trade_no(pay.getMch_code());
            merchantReqNotify.setService(pay.getPay_kind_code());
            merchantReqNotify.setRemark(pay.getRemarks());
            if (pay.getStatus() == MpPay.STATUS_PAY_SUCCESS) {
                merchantReqNotify.setStatus(1);
            } else {
                merchantReqNotify.setStatus(0);
            }
            merchantReqNotify.setTime_expire(pay.getExpires().getTime() + "");
            merchantReqNotify.setTime_start(pay.getBegin().getTime() + "");
            merchantReqNotify.setTotal_fee(pay.getFee());
            int status = getSendMchCallbackStatus(merchantReqNotify, mpMerchant.getSecret_key(), pay.getCallback_url());
            log.info("通知商户状态*:{}", status);
            //更新通知状态
            if (status == MchHttpUtil.SEND_STATUS_SUCCESS) {
                int result = payDao.updatePayMerchantNotifyStatusByCode(pay.getCode(), 1);
                this.sendMsgToMerchantChatGroup(mpMerchant,pay);
                log.info("1更新通知状态结果：{}", result);
            } else if (status == MchHttpUtil.SEND_STATUS_FAIL) {
                int result = payDao.updatePayMerchantNotifyStatusByCode(pay.getCode(), 0);
                log.info("2更新通知状态结果：{}", result);
            } else if (status == MchHttpUtil.SEND_STATUS_TIME_OUT) {
                int result = payDao.updatePayMerchantNotifyStatusByCode(pay.getCode(), 0);
                log.info("3更新通知状态结果：{}", result);
            }
            return status;
        }
        return MchHttpUtil.SEND_STATUS_FAIL;
    }


    @HystrixCommand(commandKey = "pay003",
            groupKey = "payGroup",
            fallbackMethod = "getSendMchCallbackStatusTimeOutFallBack",
            ignoreExceptions = {Exception.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")//6秒超时
            })
    public int getSendMchCallbackStatus(MerchantReqNotify merchantReqNotify, String key, String url) {
        return MchHttpUtil.sendMchCallBack(merchantReqNotify, key, url);
    }


    public int getSendMchCallbackStatusTimeOutFallBack(MerchantReqNotify merchantReqNotify, String key, String url) {
        return MchHttpUtil.SEND_STATUS_TIME_OUT;
    }


    @Async
    @Override
    public void withDrawalCallBackNotify(String code) {
        //Withdrawal withdrawal = withdrewDao.getWithDrawalByCode(code);
        //根据订单信息通知商户

    }
    //发补单消息到商户群
    private void sendMsgToMerchantChatGroup(MpMerchant mpMerchant,MpPay pay){
        //商户联系人保存:是否开启补单推送,群聊ID
        String[] str= StringUtils.isNotBlank(mpMerchant.getContact_name()) ?
                mpMerchant.getContact_name().split(",") : null;
        if(str!=null && str.length>1){
            String isOpen=str[0];
            if("1".equals(isOpen)) {
                String chatId=str[1];
                if(StringUtils.isBlank(chatId)){
                    return;
                }
                //完成时间和下单时间相隔10分钟以上视为掉单
                long minutes=(pay.getEnd().getTime()-pay.getBegin().getTime())/(1000*60);
                if(minutes>=8L) {
                    log.info("{}发送消息到群组：{}", pay.getCode(),mpMerchant.getName());
                    StringBuffer html = new StringBuffer();
                    html.append(String.format("%s 成功哦！！！", pay.getMch_code()));
                    telegramNotifyService.sendMsgToMerchantChatGroup(teleurl, chatId, URLEncoder.encode(html.toString()));
                }
            }
        }
    }
}
