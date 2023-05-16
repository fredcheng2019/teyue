package com.ruoyi.merchant.domain;

import java.math.BigDecimal;


public class HttpReqPlatformWithdrew {
	
	private BigDecimal total_fee;//交易金额，分
	
	private String merchant_id;//商户id
	
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

	public BigDecimal getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public long getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(long channel_id) {
		this.channel_id = channel_id;
	}

	public long getWallet_kind_id() {
		return wallet_kind_id;
	}

	public void setWallet_kind_id(long wallet_kind_id) {
		this.wallet_kind_id = wallet_kind_id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getBank_sub_branch() {
		return bank_sub_branch;
	}

	public void setBank_sub_branch(String bank_sub_branch) {
		this.bank_sub_branch = bank_sub_branch;
	}

	public String getBank_province() {
		return bank_province;
	}

	public void setBank_province(String bank_province) {
		this.bank_province = bank_province;
	}

	public String getBank_city() {
		return bank_city;
	}

	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}

	public String getBank_union_no() {
		return bank_union_no;
	}

	public void setBank_union_no(String bank_union_no) {
		this.bank_union_no = bank_union_no;
	}

	public String getBank_account_owner() {
		return bank_account_owner;
	}

	public void setBank_account_owner(String bank_account_owner) {
		this.bank_account_owner = bank_account_owner;
	}

	public String getBank_account_no() {
		return bank_account_no;
	}

	public void setBank_account_no(String bank_account_no) {
		this.bank_account_no = bank_account_no;
	}

	public int getBank_account_type() {
		return bank_account_type;
	}

	public void setBank_account_type(int bank_account_type) {
		this.bank_account_type = bank_account_type;
	}
}
