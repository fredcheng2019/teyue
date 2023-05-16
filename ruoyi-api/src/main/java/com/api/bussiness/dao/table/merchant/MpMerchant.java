package com.api.bussiness.dao.table.merchant;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class MpMerchant {

    private Long id;

    private String name;

    private String code;

    private int status;

    private String account;

    private String password;

    private String contact_name;

    private String contact_phone;

    private String contact_email;

    private String secret_key;

    private Long agent_id;

    private BigDecimal agent_profit;

    private Integer is_agent;


}
