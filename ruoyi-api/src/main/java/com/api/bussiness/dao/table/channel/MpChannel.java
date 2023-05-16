package com.api.bussiness.dao.table.channel;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MpChannel {

	private Long id;

	private String name;

	private String code;

	private int status;

	private int withdrawal_level;

	private String contact_name;

	private String contact_phone;

	private String contact_email;

	private BigDecimal withdrawal_fee;

	private BigDecimal mch_withdrawal_fee;

	private String class_name;

	private String req_url;

	private BigDecimal withdrawal_limt_left;

	private BigDecimal withdrawal_limt_right;

	private String secret_key;

	private int withdrawal_inner;//0内扣、1外口

    private String channelMethodCode;

}
