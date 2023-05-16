package com.api.bussiness.dao.table.merchant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MpMerchantBank {
	
	
	private long id;
	
	private long merchant_id;
	
	private long bank_id;
	
	private String bank_branch;
	
	private String bank_sub_branch;
	
	private String bank_province;
	
	private String bank_city;
	
	private String bank_union_no;
	
	private String bank_account_owner;
	
	private String bank_account_no;
	
	private int bank_account_type;

}
