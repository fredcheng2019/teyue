package com.api.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUtil {

    private static Logger log = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 签名算法
     *
     * @param str 参与签名的拼接报文数据对象
     * @param key
     * @return 签名
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    public static String getSignReq(String str, String key) {
        String[] levelOne = str.split("&");
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < levelOne.length; i++) {
            String[] levelTwo = levelOne[i].split("=");
            if (null != levelTwo) {
                if(levelTwo.length == 2){
                    if (StringUtils.isEmpty(levelTwo[1])) {
                        continue;
                    }
                    if (!levelTwo[0].equals("sign")) {
                        list.add(levelOne[i] + "&");
                    }
                }
                //body=dafacloud&mch_id=10015&notify_url=http%3A%2F%2Fpay.66tc66lsuz.com%2FShouXinYiReturn%2FIndex&out_trade_no=RK400595292994065096&remark=dafacloud&service=603&total_fee=5000&key=055ffe74d9dabf12da8ec10d1ba573f9
                else if (levelTwo.length > 2){
                    if (!levelTwo[0].equals("sign")) {
                        String levelSecondValue= levelOne[i];
                        list.add(levelSecondValue + "&");
                    }
                }
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        log.info("签名：" + result);
        try {
            String resultRef = URLDecoder.decode(result, "UTF-8");
            result = resultRef;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("签名：" + result);
        result = MD5.MD5Encode(result).toUpperCase();//MD5
        return result;
    }



    /**
     * 获取签名算法后的拼接报文
     *
     * @param o   参与签名的json数据对象
     * @param key
     * @return 签名
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    public static String getSignReq(Object o, String key)
            throws IllegalAccessException, UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<String>();
        JSONObject jobj = JSONObject.fromObject(o);
        //获取json中的key
        Iterator it = jobj.keys();
        while (it.hasNext()) {
            String jsonkey = it.next().toString();
            if (!"sign".equals(jsonkey) && jobj.getString(jsonkey) != null && !"".equals(jobj.getString(jsonkey))) {
                if (!(jobj.get(jsonkey) instanceof JSONArray)) {
                    list.add(jsonkey + "=" + jobj.getString(jsonkey) + "&");
                }
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        StringBuilder oriSb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            oriSb.append(arrayToSort[i]);
        }
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        System.out.println(result.getBytes("UTF-8"));
        System.out.println("result:" + result);
        result = MD5.MD5Encode(result).toUpperCase();//MD5
        oriSb.append("sign=");
        oriSb.append(result);
        System.out.println(oriSb);
        return oriSb.toString();
    }


    public static Map<String,String> getSignReqs(Object o, String key) throws IllegalAccessException, UnsupportedEncodingException {
        //String signReq = "body=yhgoods&finish_time=1602413697000&mch_id=Z4UOUZT3ZG26ZKNJ&order_date=1602413616000&order_sn=202010111853360001&out_trade_no=WX202010111853345392&service=aliwap&status=1&time_expire=1602414216000&time_start=1602413616000&total_fee=20000.0000&key=KWJRQPSYEQJJCAQS";
        String signReq = getSignReq(o, key);
        String[] split = signReq.split("&");
        Map<String, String> params = new HashMap<>();
        for (String s : split) {
            String[] split1 = s.split("=");
            params.put(split1[0], split1[1]);
        }
        return params;
    }



    /**
     * 根据拼接串得到Map
     */
    public static Map<String, Object> getMapFromStr(String str) {

        if (null == str || str.length() < 10) {
            return null;
        }

        Map<String, Object> keyValues = new HashMap<>();

        String[] levelOne = str.split("&");
        if (null != levelOne && levelOne.length > 0) {
            for (int i = 0; i < levelOne.length; i++) {
                String[] levelSecond = levelOne[i].split("=");
                if (null != levelSecond && levelSecond.length > 0) {
                    if (levelSecond.length == 1) {
                        keyValues.put(levelSecond[0], "");
                    } else if(levelSecond.length == 2){
                        keyValues.put(levelSecond[0], levelSecond[1]);
                    }else{
                        //notify_url=http://pay08.skyecdn.com/recharge_notify?p=MixDNVMyUzhTNDY3NjE0Nix5c2Z6ay1zeHl6Zg
                        //兼容url的?号带参方式,截取notify_url=后面那串
                        String levelSecondValue=levelOne[i].substring(levelOne[i].indexOf("=")+1);
                        keyValues.put(levelSecond[0], levelSecondValue);
                    }

                }
            }
        } else {
            return null;
        }
        return keyValues;
    }

    public static void main(String[] args) {
        String aa="body=body&mch_id=10109&notify_url=http%3A%2F%2Fpay08.skyecdn.com%2Frecharge_notify%3Fp=MixDNVMyUzhTNDcwNzU0MCx5c2Z6ay1zeHl6Zg==&out_trade_no=C5S2S8S4707540&remark=remark&service=111&sign=BEA1E1B7A472CC578CECF1054D0A925D&total_fee=20000";
        Map<String, Object> map = getMapFromStr(aa);
        for (String key : map.keySet()) {
            System.out.println("key=" + key + ",value=" + map.get(key));
        }
        String str= getSignReq(aa,"aa");
    }

}
