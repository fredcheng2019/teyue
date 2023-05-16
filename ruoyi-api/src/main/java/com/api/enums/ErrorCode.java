package com.api.enums;

public class ErrorCode {
	
	public static final String SUCCESS                     = "成功";
	
	public static final String ILLEGAL_PARAMETER           = "请求参数非法";
	
	public static final String EMPTY_MERCHANT_ID           = "商户号不能为空";
	
	public static final String EMPTY_MERCHANT_ORDER_CODE   = "商户订单号不能为空";
	
	public static final String EMPTY_CALLBACK_URL          = "异步通知url不能为空";
	
	public static final String EMPTY_FEE                   = "交易金额不能为空";
	
	public static final String EMPTY_SIGN                  = "验签字段不能为空";
	
	public static final String ERROR_SIGN                  = "签名错误";
	
	public static final String EMPTY_MERCHANT_INFO         = "商户信息不存在";
	
	public static final String PAUSE_MERCHANT              = "商户被停用";
	
	public static final String EXIST_MERCHANT_ORDER_CODE   = "订单号已存在";
	
	public static final String ILLEGAL_FEE                 = "交易金额非法，不能有角分";
	
	public static final String EMPTY_QR_CODE_TYPE          = "二维码类型不能为空";
	
	public static final String merchant_excess             = "账号当日已超额";
	
	public static final String FAIL_                       = "交易失败（应当发起交易查询）";
	
	public static final String SYSTEM_ABNORMAL             = "系统异常（应当发起交易查询）";
	
	public static final String UNKNOW_TRANSACTION_RESULT   = "交易结果未知（应当发起交易查询）";
	
	public static final  String UNKNOW_IP                  = "未知IP";
	

}
