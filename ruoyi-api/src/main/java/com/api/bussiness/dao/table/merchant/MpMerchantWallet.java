package com.api.bussiness.dao.table.merchant;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MpMerchantWallet {
	
	private Long id;

	private Long wallet_kind_id;
		
	private Long merchant_id;
	
	private BigDecimal account_balance;
	
	private BigDecimal frozen_balance;
	
	private BigDecimal unliquidated_balance_one;
	
	private BigDecimal unliquidated_balance_two;
	
	private BigDecimal d_zero;

	private BigDecimal advance_balance;

}
