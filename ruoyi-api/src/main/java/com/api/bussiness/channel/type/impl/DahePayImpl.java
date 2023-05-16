package com.api.bussiness.channel.type.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.base.util.MD5;
import com.api.bussiness.channel.context.*;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.channel.util.CommonUtil;
import com.api.bussiness.channel.util.HttpsUtil;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.order.MpPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * @auther Lengff
 * @time 2020/10/5
 * @fileName UnKnownPayService
 */
public class DahePayImpl implements ChannelMethod {
    private static Logger log = LoggerFactory.getLogger(DahePayImpl.class);
    //https://p.vipdahe.top/
    private final String payUrl = "/api/v3/cashier.php";
    private final String queryUrl = "/api/v3/query.php";

    @Override
    public ChannelRspPay pay(ChannelReqPay channelReqPay) {
        /**
         商户号	merchant	是	是	商户号，由支付平台提供
         类型	qrtype	是	是	支付类型：
         固定值 wp:微信扫码，ap:支付宝扫码, aph5:支付宝H5
         商户订单号	customno	是	是	商户系统订单号，商户需保证该参数的唯一性
         不可以使用特殊符号
         订单金额	money	是	是	以“元”为单位，两位小数，必须大于零

         订单时间	sendtime	是	是	商户系统的订单时间, 使用10位数UNIX时间戳
         异步通知地址	notifyurl	是	是	服务端通知地址,支付成功后。支付平台将发送相关信息至该地址,通知商户支付结果。商户收到信息后,进行业务处理并返回字符串“OK”（大写）,表明已收到通知,返回其他信息均为失败。
         格式：http / https 不要编码
         在收银台跳转到商户指定的地址	backurl	是	是	客户在收银台扫码付款时，可通过按钮返回商户页面
         不要编码
         风险级别	risklevel	是	是	固定值数字1-5， 系统将根据风险级别分配相应的收款号给客户，如果空表示不限制风险级别。填写1即可
         签名	sign	是	-	32位小写md5签名值
         */
        try {
            MpChannel mpChannel = channelReqPay.getMpChannel();
            MpPay mpPay = channelReqPay.getMpPay();
            // 参数信息
            String merchant_no = mpPay.getChannel_code();
            String order_no = mpPay.getCode();
            String amount = mpPay.getFee().divide(new BigDecimal(100D)).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
            String pay_type = mpChannel.getChannelMethodCode();
            String notify_url = channelReqPay.getChannel_notify_url();
            String return_url = channelReqPay.getChannel_redirect_url();
            String risklevel="1";
            String attch = mpPay.getCode();
            long timestamp = System.currentTimeMillis()/1000;
            // 组合参数信息
            LinkedHashMap<String, String> param = new LinkedHashMap<>();
            param.put("merchant", merchant_no);
            param.put("qrtype", pay_type);
            param.put("customno", order_no);
            param.put("money", amount);
            param.put("sendtime",timestamp+"");
            param.put("notifyurl", notify_url);
            param.put("backurl", return_url);
            param.put("risklevel", risklevel);

            String s = "merchant=%s&qrtype=%s&customno=%s&money=%s&sendtime=%s&notifyurl=%s&backurl=%s&risklevel=%s%s";
            String sort = String.format(s,merchant_no,pay_type,order_no,amount,timestamp,notify_url,return_url,risklevel,mpChannel.getSecret_key());
            log.debug("Dahe下单请求参数sort is " + sort);
            String sign = MD5.MD5Encode(sort).toLowerCase();
            log.info("Dahe下单请求加密后sign为:" + sign);
            param.put("sign", sign);
            log.info("Dahe下单请求参数为:" + JSON.toJSONString(param));
//            String resp = HttpsUtil.doPost(mpChannel.getReq_url() + payUrl ,param);
//            log.info("Dahe下单请求响应参数为:" + resp);
//            JSONObject respJson = JSON.parseObject(resp);
//            if (!"0".equals(respJson.getString("errCode"))) {
//                // 下单失败
//                return new ChannelRspPay(ChannelRspPay.RSP_STATUS_PAY_FAIL, mpChannel.getClass_name(), respJson.getString("rspmsg"));
//            }
//            JSONObject payParams = respJson.getJSONObject("data");
//            String qrcode = payParams.getString("data");
            param.put("req_url", mpChannel.getReq_url() + payUrl);
            String url = channelReqPay.getJump_pay_url() + "?" + CommonUtil.createLinkString(param);
            // 下单成功
            return new ChannelRspPay(ChannelRspPay.RSP_STATUS_PAY_SUCCESS, mpPay.getCode(), url, "下单成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return new ChannelRspPay(ChannelRspPay.RSP_STATUS_PAY_FAIL, e.getMessage());
        }
    }

    @Override
    public ChannelRspCheckPay checkPay(ChannelReqCheckPay channelReqCheckPay) {
        try {
            /**
             商户号	merchant	是	是	商户号，由支付平台提供
             商户订单号	customno	是	是	商户系统订单号
             查询时间	sendtime	是	是	商户系统提交查询请求时间 使用10位数UNIX时间戳
             签名	sign	是	-	32位小写md5签名值
             */
            MpPay mpPay = channelReqCheckPay.getMpPay();
            String merchant_no = mpPay.getChannel_code();
            String out_order_no = mpPay.getCode();
            long timestamp = System.currentTimeMillis()/1000;

            LinkedHashMap<String, String> param = new LinkedHashMap<>();
            param.put("merchant", merchant_no);
            param.put("customno", out_order_no);
            param.put("sendtime", timestamp+"");

            String s = "merchant=%s&customno=%s&sendtime=%s%s";
            String sort = String.format(s,merchant_no,out_order_no,timestamp,channelReqCheckPay.getMpChannel().getSecret_key());
            log.debug("Dahe查单请求参数sort is " + sort);
            String sign = MD5.MD5Encode(sort ).toLowerCase();
            log.info("Dahe查单请求加密后sign为:" + sign);
            param.put("sign", sign);
            log.info("Dahe查单请求查询订单请求参数为:" + JSON.toJSONString(param));
            String resp = HttpsUtil.doPost(channelReqCheckPay.getMpChannel().getReq_url() + queryUrl , param);
            log.info("Dahe查单请求查询订单请求响应数据为:" + resp);
            JSONObject respJson = JSON.parseObject(resp);
            // 判断请求是否成功
            if (!"0".equals(respJson.getString("errCode"))) {
                log.warn("Dahe查单请求查询订单异常:返回接口信息异常!");
                return new ChannelRspCheckPay(ChannelRspPay.RSP_STATUS_PAY_FAIL, "查单失败");
            }
            JSONObject payParams = respJson.getJSONObject("data");
            String money = payParams.getString("money");
            String payStatus = payParams.getString("state");
            // 校验订单是否支付
            if (!"1".equals(payStatus)) {
                log.warn("Dahe查单请求异常:返回接口显示支付失败 (true 成功 false 失败)" + payStatus);
                return new ChannelRspCheckPay(ChannelRspPay.RSP_STATUS_PAY_FAIL, "订单查询显示未支付");
            }
            // 判断订单金额是否一致
            if (mpPay.getFee().divide(new BigDecimal(100)).doubleValue() != Double.valueOf(money).doubleValue()) {
                log.warn("Dahe查单请求异常,查询金额和原订单金额不一致!");
                return new ChannelRspCheckPay(ChannelRspPay.RSP_STATUS_PAY_FAIL, "查询金额和原订单金额不一致!");
            }
            ChannelRspCheckPay notify = new ChannelRspCheckPay(ChannelRspPay.RSP_STATUS_PAY_SUCCESS, "订单查询成功!");
            notify.setCh_code(mpPay.getCh_code());
            notify.setCode(mpPay.getCode());
            notify.setFee(mpPay.getFee());
            return notify;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ChannelRspCheckPay(ChannelRspPay.RSP_STATUS_PAY_FAIL, e.getMessage());
        }
    }

    @Override
    public ChannelRspWithdrawal withdrawal(ChannelReqWithdrawal channelReqWithdrawal) {
        return null;
    }

    @Override
    public ChannelRspCheckWithdrawal checkWithdrawal(ChannelReqCheckWithdrawal channelReqCheckWithdrawal) {
        return null;
    }

    @Override
    public ChannelRspCheckBalance checkBalance(ChannelReqCheckBalance channelReqCheckBalance) {
        return null;
    }

    @Override
    public ChannelRspPayNotify parsePayNotify(String msg, String channel_code, String channel_secret_key) {
        /**
         商户号	merchant	是	是	商户号，由支付平台提供
         类型	qrtype	是	是	支付类型
         固定值：wp微信；ap支付宝；aph5支付宝H5
         商户订单号	customno	是	是	请求时商户传入的customno
         订单时间	sendtime	是	是	请求时商户传入的sendtime
         支付平台订单号	orderno	是	是	支付平台系统产生的订单号，格式：数据字母组成
         订单金额	money	是	是	客户实际支付金额，该参数与请求时商户传入的money相同。以“元”为单位，两位小数
         支付时间	paytime	是	是	客户支付时间，10位数UNIX时间戳
         订单状态	state	是	是	固定值：1(支付成功)
         暂时只对支付成功的订单回调通知
         签名	sign	是	-	32位小写md5签名值
         */
        try {
            JSONObject param = JSON.parseObject(msg);
            String merchant = param.getString("merchant");
            String qrtype = param.getString("qrtype");
            String customno = param.getString("customno");
            String sendtime = param.getString("sendtime");
            String orderno = param.getString("orderno");
            String money = param.getString("money");
            String paytime = param.getString("paytime");
            String state = param.getString("state");
            String sign = param.getString("sign");
            String s = "merchant=%s&qrtype=%s&customno=%s&sendtime=%s&orderno=%s&money=%s&paytime=%s&state=%s%s";
            String sort = String.format(s,merchant,qrtype,customno,sendtime,orderno,money,paytime,state,channel_secret_key);
            log.debug("Dahe下单请求参数sort is " + sort);
            String mySign = MD5.MD5Encode(sort).toLowerCase();
            log.debug("Dahe回调验证我方sign" + mySign);
            // 验证签名
            if (!mySign.equals(sign)) {
                log.info("Dahe回调验证：回调签名错误：" + mySign + "->" + sign);
                return new ChannelRspPayNotify(ChannelRspPayNotify.RSP_STATUS_NOTIFY_PAY_FAIL, "系统验签错误");
            }
            if (!"1".equals(state)) {
                // true 成功 false 失败
                log.warn("Dahe回调验证：：回调显示支付异常");
                return new ChannelRspPayNotify(ChannelRspPayNotify.RSP_STATUS_NOTIFY_PAY_FAIL, "回调显示支付异常");
            }
            log.info("Dahe回调验证：支付回调成功:OK");
            ChannelRspPayNotify pay = new ChannelRspPayNotify(ChannelRspPayNotify.RSP_STATUS_NOTIFY_PAY_SUCCESS, "订单回调成功");
            pay.setChannel_code(channel_code);
            pay.setCh_code(orderno);
            pay.setFee(new BigDecimal(money).multiply(new BigDecimal(100)));
            pay.setCode(customno);
            pay.setRspMsg("OK");
            return pay;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ChannelRspPayNotify(ChannelRspPayNotify.RSP_STATUS_NOTIFY_PAY_FAIL, e.getMessage());
        }
    }

    @Override
    public ChannelRspWithdrawalNotify parseWithDrawalNotify(String msg, String channel_code, String channel_secret_key) {
        return null;
    }

    public static void main(String[] args) throws Exception{
        String merchant_no ="YB21011521054";
        String order_no = System.currentTimeMillis()+"";
        String amount = "10.00";
        String pay_type = "ap";
        String notify_url = "http://baidu.com";
        String return_url = "http://baidu.com";
        String attch = "";
        long timestamp = System.currentTimeMillis()/1000;
        // 组合参数信息
        LinkedHashMap<String, String> param = new LinkedHashMap<>();
        param.put("merchant", merchant_no);
        param.put("qrtype", pay_type);
        param.put("customno", order_no);
        param.put("money", amount);
        param.put("sendtime",timestamp+"");
        param.put("notifyurl", notify_url);
        param.put("backurl", return_url);
        param.put("risklevel", "1");

        String s = "merchant=%s&qrtype=%s&customno=%s&money=%s&sendtime=%s&notifyurl=%s&backurl=%s&risklevel=%s%s";
        String sort = String.format(s,merchant_no,pay_type,order_no,amount,timestamp,notify_url,return_url,"1","c3d9587bd60c88109c11460845fd8079");
        log.debug("Dahe下单请求参数sort is " + sort);
        String sign = MD5.MD5Encode(sort).toLowerCase();
        log.info("Dahe下单请求加密后sign为:" + sign);
        param.put("sign", sign);
        log.info("Dahe下单请求参数为:" + JSON.toJSONString(param));
        String resp = HttpsUtil.doPost("https://p.vipdahe.top/api/v3/cashier.php" ,param);
        log.info("Dahe下单请求响应参数为:" + resp);
    }
}
