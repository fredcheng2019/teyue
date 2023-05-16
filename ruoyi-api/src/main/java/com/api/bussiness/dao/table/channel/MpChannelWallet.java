package com.api.bussiness.dao.table.channel;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MpChannelWallet {
	
	private int id;
	
	private int channel_id;
	
	//private int wallet_kind_id;
	
	private BigDecimal account_balance;
	
	private BigDecimal frozen_balance;

}
