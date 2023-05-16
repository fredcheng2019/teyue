package com.api.enums;

/**
 * @author CCX
 *系统常量
 * @date:2016-3-4 下午3:29:22
 * @version :
 * 
 */
public class Global {
	/**成功状态码*/
	public final static int SUCCESS_CODE = 0;
	/**成功返回信息*/
	public final static String SUCCESS_MSG ="成功";
	public final static int FAIL_CODE =1;
	/**报文格式不正确*/
	public final static int FAIL_JSON_CODE = 1001;
	/**报文格式不正确返回信息*/
	public final static String FAIL_JSON_MSG = "报文格式不正确";
	/**sign错误错误码*/
	public final static int FAIL_SIGN_CODE = 1002;
	/**sign错误返回信息*/
	public final static String FAIL_SIGN_MSG = "签名验证失败！";
	/**服务器异常 */
	public final static String FAIL_SERVER_ERR_MSG = "服务器异常！";
	
	
}
