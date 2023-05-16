package com.api.bussiness.dao.table.withdrawal;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Withdrawal {
	//状态 1下单失败 2下单超时 3待结算 4未代付 5已代付6审核中
	public static final int STATUS_ORDER_FAIL         = 1;
	
	public static final int STATUS_ORDER_TIME_OUT     = 2;
	
	public static final int STATUS_FROZING            = 3;

	public static final int STATUS_WITHDRAWAL_FAIL    = 4;

	public static final int STATUS_WITHDRAWAL_SUCCESS = 5;
	
	public static final int STATUS_WITHDRAWAL_APPLY = 6;
	
	private long id;
	
	private int status;
	
	private String code;
	
	private String ch_code;
	
	private String mch_code;
	
	private BigDecimal fee;
	
	private BigDecimal sub_fee;
	
	private BigDecimal ch_fee;
	
	private BigDecimal mch_fee;
	
	private BigDecimal fin_fee;
	
	private Date begin;
	
	private Date end;
	
	private long merchant_id;
	
	private String merchant_name;
	
	private String merchant_code;
	
	private long channel_id;
	
	private String channel_name;
	
	private String channel_code;
	
	private BigDecimal channel_withdrawal_fee;
	
	private BigDecimal channel_mch_withdrawal_fee;
	
	private long wallet_kind_id;
	
	private String wallet_kind_name;
	
	private String wallet_kind_code;
		
	private String bank_name;
	
	private String bank_code;
	
	private String bank_branch;
	
	private String bank_sub_branch;
	
	private String bank_province;
	
	private String bank_city;
	
	private String bank_union_no;
	
	private String bank_account_owner;
	
	private String bank_account_no;
	
	private int bank_account_type;
	
	private String callback_url;
	
	private String channel_class_name;
	
	private int merchant_notify_status;
	

}
