package com.api.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sifang.system")
@Getter
@Setter
public class SystemProperties {
	
	/**
	 * 通知回调地址
	 */
	private String notifyUrl;

	/**
	 * 跳转地址
	 */
	private String redirectUrl;
	
	/**
	 * 支付中转地址
	 */
	private String jumpPayUrl;
	
	/**
	 * 平台白名单
	 */
	private String platformWhiteList;

}
