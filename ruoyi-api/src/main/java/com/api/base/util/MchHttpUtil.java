package com.api.base.util;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.channel.util.HttpsUtil;
import com.api.bussiness.merchant.bean.MerchantReqNotify;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

public class MchHttpUtil {

    private static Logger log = LoggerFactory.getLogger(MchHttpUtil.class);

    public static final int SEND_STATUS_SUCCESS = 0;//成功

    public static final int SEND_STATUS_FAIL = 1;//失败

    public static final int SEND_STATUS_TIME_OUT = 2;//超时


    public static int sendMchCallBack(MerchantReqNotify merchantReqNotify, String key, String url) {

        try {
            // String str = SignUtil.getSignReq(merchantReqNotify, key);
            //long start = System.currentTimeMillis();
            // Post请求的url，与get不同的是不需要带参数
            URL postUrl = new URL(URLDecoder.decode(url, "utf-8"));
            log.debug(postUrl.toString());
            Map<String, String> signReqs = SignUtil.getSignReqs(merchantReqNotify, key);
            log.info("回调参数："+signReqs);
            String result = HttpsUtil.doPost(postUrl.toString(), signReqs);
            log.info(signReqs+"回调结果："+result);
            /*// 打开连接
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/text");
            connection.setConnectTimeout(5000);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.write(str.getBytes());
            out.flush();
            out.close(); // flush and close
            connection.getInputStream();
            long end = System.currentTimeMillis();
            log.info("运行时间：" + (end - start) + "毫秒");
            // 获取响应头信息
            // 开始获取数据
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            String result = bos.toString("utf-8");
            log.info("结果：" + result);
            connection.disconnect();
            out.close();
            bis.close();
            bos.close();*/
            //尝试用json格式 解释
            try {
                MerchantRspErrorResult rspErr = JSON.parseObject(result, MerchantRspErrorResult.class);
                if (rspErr.getCode().equals("success")) {
                    return MchHttpUtil.SEND_STATUS_SUCCESS;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                if (result.trim().startsWith("success")) {
                    return MchHttpUtil.SEND_STATUS_SUCCESS;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return MchHttpUtil.SEND_STATUS_FAIL;
        }

        return MchHttpUtil.SEND_STATUS_TIME_OUT;
    }

}
