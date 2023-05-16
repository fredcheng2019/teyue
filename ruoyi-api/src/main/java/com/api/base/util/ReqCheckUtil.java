package com.api.base.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.enums.ErrorCode;


public class ReqCheckUtil {
	
	private static Logger log = LoggerFactory.getLogger(ReqCheckUtil.class);
	
	
	public static String checkPlaceAnOrder(Map<String,Object> reqMap) {
		
		//商户号字段
		if(reqMap.containsKey("mch_id")) {
			if(null == reqMap.get("mch_id") || reqMap.get("mch_id").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ID;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ID;
		}
		
		//订单号字段
		if(reqMap.containsKey("out_trade_no")) {
			if(null == reqMap.get("out_trade_no") || reqMap.get("out_trade_no").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
		}
		
		//签名字段
		if(reqMap.containsKey("sign")) {
			if(null == reqMap.get("sign") || reqMap.get("sign").equals("")) {
				return ErrorCode.EMPTY_SIGN;
			}
		}else {
			return ErrorCode.EMPTY_SIGN;
		}
		
		//异步url
		if(reqMap.containsKey("notify_url")) {
			if(null == reqMap.get("notify_url") || reqMap.get("notify_url").equals("")) {
				return ErrorCode.EMPTY_CALLBACK_URL;
			}
		}else {
			return ErrorCode.EMPTY_CALLBACK_URL;
		}
		
		//交易金额
		if(reqMap.containsKey("total_fee")) {
			if(null == reqMap.get("total_fee") || reqMap.get("total_fee").equals("")) {
				return ErrorCode.EMPTY_FEE;
			}else {
				//判断是否是正常金额
				try {
					Integer.parseInt(reqMap.get("total_fee")+"");
				}catch (Exception e) {
					return ErrorCode.ILLEGAL_FEE;
				}
			}
		}else {
			return ErrorCode.EMPTY_FEE;
		}
		
		//二维码类型
		if(reqMap.containsKey("service")) {
			if(null == reqMap.get("service") || reqMap.get("service").equals("")) {
				return ErrorCode.EMPTY_FEE;
			}
		}else {
			return ErrorCode.EMPTY_FEE;
		}
		
		//商品描述
		if(reqMap.containsKey("body")) {
			if(null == reqMap.get("body") || reqMap.get("body").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_INFO;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_INFO;
		}
		
		return ErrorCode.SUCCESS;
	}
	
	
	public static String checkOrder(Map<String,Object> reqMap) {
		
		//商户号字段
		if(reqMap.containsKey("mch_id")) {
			if(null == reqMap.get("mch_id") || reqMap.get("mch_id").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ID;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ID;
		}
		
		//订单号字段
		if(reqMap.containsKey("out_trade_no")) {
			if(null == reqMap.get("out_trade_no") || reqMap.get("out_trade_no").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
		}
		
		//签名字段
		if(reqMap.containsKey("sign")) {
			if(null == reqMap.get("sign") || reqMap.get("sign").equals("")) {
				return ErrorCode.EMPTY_SIGN;
			}
		}else {
			return ErrorCode.EMPTY_SIGN;
		}
		
		return ErrorCode.SUCCESS;
	}
	
	
	public static String checkWithdrawal(Map<String,Object> reqMap) {
		
		//商户号字段
		if(reqMap.containsKey("mch_id")) {
			if(null == reqMap.get("mch_id") || reqMap.get("mch_id").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ID;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ID;
		}
		
		//订单号字段
		if(reqMap.containsKey("out_trade_no")) {
			if(null == reqMap.get("out_trade_no") || reqMap.get("out_trade_no").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
		}
		
		//签名字段
		if(reqMap.containsKey("sign")) {
			if(null == reqMap.get("sign") || reqMap.get("sign").equals("")) {
				return ErrorCode.EMPTY_SIGN;
			}
		}else {
			return ErrorCode.EMPTY_SIGN;
		}
		
		//交易金额
		if(reqMap.containsKey("total_fee")) {
			if(null == reqMap.get("total_fee") || reqMap.get("total_fee").equals("")) {
				return ErrorCode.EMPTY_FEE;
			}else {
				//判断是否是正常金额
				try {
					Integer.parseInt(reqMap.get("total_fee")+"");
				}catch (Exception e) {
					return ErrorCode.ILLEGAL_FEE;
				}
			}
		}else {
			return ErrorCode.EMPTY_FEE;
		}
		
		//持卡人名称
		if(reqMap.containsKey("bank_car_name")) {
			if(null == reqMap.get("bank_car_name") || reqMap.get("bank_car_name").equals("")) {
				return "持卡人名称不能为空";
			}
		}else {
			return "持卡人名称不能为空";
		}
		
		//银行卡账户
		if(reqMap.containsKey("bank_car_no")) {
			if(null == reqMap.get("bank_car_no") || reqMap.get("bank_car_no").equals("")) {
				return "银行卡账户字段不能为空";
			}
		}else {
			return "银行卡账户字段不能为空";
		}
		
		//开户银行代码
		if(reqMap.containsKey("bank_type")) {
			if(null == reqMap.get("bank_type") || reqMap.get("bank_type").equals("")) {
				return "开户银行代码字段不能为空";
			}
		}else {
			return "开户银行代码字段不能为空";
		}
		
		//开户分行
		if(reqMap.containsKey("bank_address")) {
			if(null == reqMap.get("bank_address") || reqMap.get("bank_address").equals("")) {
				return "开户分行字段不能为空";
			}
		}else {
			return "开户分行字段不能为空";
		}
		
		//支行名称
		if(reqMap.containsKey("branch")) {
			if(null == reqMap.get("branch") || reqMap.get("branch").equals("")) {
				return "支行名称字段不能为空";
			}
		}else {
			return "支行名称字段不能为空";
		}
		
		//开户省
		if(reqMap.containsKey("bank_province")) {
			if(null == reqMap.get("bank_province") || reqMap.get("bank_province").equals("")) {
				return "开户省字段不能为空";
			}
		}else {
			return "开户省字段不能为空";
		}
		
		//开户市
		if(reqMap.containsKey("bank_city")) {
			if(null == reqMap.get("bank_city") || reqMap.get("bank_city").equals("")) {
				return "开户市字段不能为空";
			}
		}else {
			return "开户市字段不能为空";
		}
		
		//钱包类型
		if(reqMap.containsKey("wallet_type")) {
			if(null == reqMap.get("wallet_type") || reqMap.get("wallet_type").equals("")) {
				return "钱包类型字段不能为空";
			}
		}else {
			return "钱包类型字段不能为空";
		}
		
		return ErrorCode.SUCCESS;
	}

	/**
	 * 使用Introspector，map集合成javabean
	 *
	 * @param map       map
	 * @param beanClass bean的Class类
	 * @return bean对象
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
	 
	    if (MapUtils.isEmpty(map)) {
	        return null;
	    }
	    try {
	        T t = beanClass.newInstance();
	        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for (PropertyDescriptor property : propertyDescriptors) {
	            Method setter = property.getWriteMethod();
	            if (setter != null) {
	                setter.invoke(t, map.get(property.getName()));
	            }
	        }
	        return t;
	    } catch (Exception ex) {
	        log.error("########map集合转javabean出错######，错误信息，{}", ex.getMessage());
	        throw new RuntimeException();
	    }
	 
	}
	
	public static String checkWithDrawalInfo(Map<String,Object> reqMap) {
		
		//商户号字段
		if(reqMap.containsKey("mch_id")) {
			if(null == reqMap.get("mch_id") || reqMap.get("mch_id").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ID;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ID;
		}
		
		//订单号字段
		if(reqMap.containsKey("out_trade_no")) {
			if(null == reqMap.get("out_trade_no") || reqMap.get("out_trade_no").equals("")) {
				return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
			}
		}else {
			return ErrorCode.EMPTY_MERCHANT_ORDER_CODE;
		}
		return ErrorCode.SUCCESS;
	}

}
