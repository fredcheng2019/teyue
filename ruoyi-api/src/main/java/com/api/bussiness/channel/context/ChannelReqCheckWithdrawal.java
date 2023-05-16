package com.api.bussiness.channel.context;

import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelReqCheckWithdrawal {
	private Withdrawal withdrawal;
	private MpChannel mpChannel;
}
