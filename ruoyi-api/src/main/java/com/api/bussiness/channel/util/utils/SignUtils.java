package com.api.bussiness.channel.util.utils;


import com.alibaba.fastjson.JSON;
import org.springframework.util.Base64Utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class SignUtils {
    public static String getSign(Map<String, String> params,String pri_key) {
        TreeMap<String, String> param = new TreeMap<String, String>(params);
        String signInfo = "";
        for (String pkey : param.keySet()) {
            signInfo += pkey + "=" + param.get(pkey) + "&";
        }
        signInfo = signInfo.substring(0, signInfo.length() - 1);
        //System.out.println("signInfo:" + signInfo);
        String sign = "";// 生成签名
        try {
            //加密
            sign = Base64Utils.encodeToString(RSAUtils.encryptByPrivateKey(signInfo.getBytes("UTF-8"), pri_key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sign:" + sign);
        return sign;
    }

    public static String getContent(Map<String,String> signParams,String password){
        return AESUtil.encryptBase64(JSON.toJSONString(SignUtils.sortMapByKey(signParams)), password);
    }

    public static String getOrderNo() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0,8);
    }

    public static String getDateTime_YYYY_MM_DD_HH_MM_SS() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}

class MapKeyComparator implements Comparator<String>{
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
