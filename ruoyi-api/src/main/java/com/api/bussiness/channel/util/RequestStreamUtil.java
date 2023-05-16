package com.api.bussiness.channel.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * requst流对象工具类
 *
 * @author LUSHUIFA242
 */
public class RequestStreamUtil {

    /**
     * 将request流中的数据转换成String
     *
     * @param request
     * @return
     */
    public static String toString(HttpServletRequest request) {
        String valueStr = "";
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            valueStr = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            valueStr = "";
        }
        return valueStr;
    }

    public static Map<String, String> getparams(HttpServletRequest request) {
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            System.out.println(responseStrBuilder.toString());
            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            System.out.println(jsonObject.toJSONString());
            return JsonToHashMap(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }


    //2.将json字符串转换成HashMap<String,String>
    public static Map<String, String> JsonToHashMap(JSONObject jsonObject) {
        HashMap<String, String> data = new HashMap<String, String>();
        try {
            @SuppressWarnings("rawtypes")
            Iterator it = jsonObject.keySet().iterator();
            // 遍历jsonObject数据，添加到Map对象
            while (it.hasNext()) {
                String key = String.valueOf(it.next()).toString();
                String value = (String) jsonObject.get(key).toString();
                data.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(data);
        return data;
    }


    /**
     * 将request流中的数据转换成Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> parameter = new HashMap<String, String>();
        String valueStr = toString(request);
        System.out.println(valueStr);
        try {
            if (null != valueStr && !"".equals(valueStr)) {
                String[] valueArr = valueStr.split("&");
                for (String kvStr : valueArr) {
                    String[] kvStrArr = kvStr.split("=");
                    parameter.put(kvStrArr[0], kvStrArr[1]);
                }
            } else {
                parameter = getParameterMap(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameter;
    }

    /**
     * 从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map<String, String> returnMap = new HashMap<String, String>();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry<String, String> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}
