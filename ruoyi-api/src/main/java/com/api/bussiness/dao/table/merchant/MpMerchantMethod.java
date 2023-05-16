package com.api.bussiness.dao.table.merchant;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MpMerchantMethod {
	
	/** 商户支付方法标识 */
	private long id;
	/** 商户支付方法费率 */
	private BigDecimal pay_rate;
	/** 商户支付方法优先级 */
	private int level;
	/** 商户支付方法状态 */
	private int status;
	/** 商户标识 */
	private long merchant_id;
	/** 商户名称 */
	private String merchant_name;
	/** 商户编码 */
	private String merchant_code;
	/** 商户状态 */
	private int merchant_tatus;
	/** 渠道支付方法标识 */
	private long channel_method_id;
	/** 渠道支付方法状态 */
	private int channel_method_status;
	/** 渠道支付方法费率（%） */
	private BigDecimal channel_method_pay_rate;
	/** 渠道支付方法限额左 */
	private BigDecimal channel_method_pay_limit_left;
	/** 渠道支付方法限额右 */
	private BigDecimal channel_method_pay_limit_right;
	/** 渠道标识 */
	private long channel_id;
	/** 渠道名称 */
	private String channel_name;
	/** 渠道编码 */
	private String channel_code;
	/** 渠道状态 */
	private int channel_status;
	/** 渠道密钥*/
	private String channel_secret_key;
	/** 渠道代付优先级 */
	private int channel_withdrawal_level;
	/** 渠道代付手续费 */
	private BigDecimal channel_withdrawal_fee;
	/** 渠道代付限额 */
	private BigDecimal channel_withdrawal_limt;
	/** 渠道商户代付手续费 */
	private BigDecimal channel_mch_withdrawal_fee;
	/** 渠道商户代付限额 */
	private BigDecimal channel_mch_withdrawal_limt;
	/** 渠道操作类 */
	private String channel_class_name;
	/** 渠道请求地址*/
	private String channel_req_url;
	/** 支付类型标识 */
	private long pay_kind_id;
	/** 支付类型名称 */
	private String pay_kind_name;
	/** 支付类型编码 */
	private String pay_kind_code;
	/** 钱包类型标识 */
	private long wallet_kind_id;
	/** 钱包类型名称 */
	private String wallet_kind_name;
	/** 钱包类型编码 */
	private String wallet_kind_code;
	
}
