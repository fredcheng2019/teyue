package com.api.bussiness.manager.bean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerReqFinanceWithdrawal {
	
	private BigDecimal total_fee;//代付金额，分
	
	private long merchant_id;//商户id
	
	private long channel_id;//渠道id
	
	private long wallet_kind_id;//钱包类型id
			
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

}
