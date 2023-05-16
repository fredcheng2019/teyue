package com.api.bussiness.channel.util;

import org.springframework.util.DigestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpDateUtil {

	public static String getSign(String params, String signKey){
		String src = params + "&key=" + signKey;
		String sign = DigestUtils.md5DigestAsHex(src.getBytes());
		return sign;
	}

	public static String getParamsSigned(Map<String, String> resultMap, String signKey){
		String params = getParams(resultMap);
		String sign = getSign(params, signKey);
		String paramsSigned = params+"&sign="+sign;
		return paramsSigned;
	}

	public static String getParamsSigned(String params, String signKey){
		String sign = getSign(params, signKey);
		String paramsSigned = params+"&sign="+sign;
		return paramsSigned;
	}

	public static String getParamsNotEmpty(Map<String, String> resultMap){
		String split = "&";
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if(val==null)
				continue;
			if (val.length()==0)
				continue;
			sb.append(key + "=" + val + split);
		}
		//剔除最后的split
		String params = sb.toString();
		if (params.length() > 0) {
			params = params.substring(0, params.length() - 1);
		}
		return  params;
	}

	public static String getParams(Map<String, String> resultMap){
		String split = "&";
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String val = entry.getValue() == null ? "" : entry.getValue();
			sb.append(key + "=" + val + split);
		}
		//剔除最后的split
		String params = sb.toString();
		if (params.length() > 0) {
			params = params.substring(0, params.length() - 1);
		}
		return  params;
	}

	public static LinkedHashMap<String, String> getParamsLinkedHashMap(String params){
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		String[] key_values = params.split("&");
		for (String key_value:key_values){
			String[] param = key_value.split("=");
			String key = param[0];
			String value = param[1];
			result.put(key,value);
		}
		return result;
	}

	public static TreeMap<String, String> getParamsTreeMap(String params){
		TreeMap<String, String> result = new TreeMap<String, String>();
		String[] key_values = params.split("&");
		for (String key_value:key_values){
			String[] param = key_value.split("=");
			String key = param[0];
			String value = param[1];
			result.put(key,value);
		}
		return result;
	}


	public static String getStrDate(String style) {
		DateFormat df = new SimpleDateFormat(style);
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		return dateName;
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime 当前时间
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
}
