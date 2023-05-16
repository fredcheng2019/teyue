package com.api.bussiness.dao.table.finance;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FinancialSetting {
	
	public static int STATUS_OPEN = 0;
	
	public static int STATUS_CLOSE = 1;
	
	private long id;
	
	private String withdrawal_password;
	
	private String google_secret;
	
	private int mch_withdrawal_rule;//代付规则（1.渠道优先级2.渠道余额大小）
	
	private int withdrawal_inner;//0内扣、1外口
	
	private int withdrawal_audit;//审核：0开启、1关闭
	
	private int channel_white;
	
	private int merchant_white;

}
