package com.api.bussiness.channel.context;

import com.api.bussiness.dao.table.channel.MpChannel;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ChannelReqCheckBalance {
	private MpChannel mpChannel;
}
