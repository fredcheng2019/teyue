package com.api.bussiness.dao.table.merchant;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FinancialWallet {
	
	private long id;
	
	private long channel_id;
	
	private BigDecimal account_balance;
	
	private BigDecimal frozen_balance;

}
