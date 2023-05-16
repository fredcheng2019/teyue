package com.api.bussiness.manager.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerRspNotifyPay {
	
	public static final int RSP_STATUS_SUCCESS = 0;//成功
	
	public static final int RSP_STATUS_FAIL    = 1;//失败
	
	private int status;
	
	private String msg;

}
