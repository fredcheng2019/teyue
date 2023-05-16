package com.api.bussiness.dao.table.log;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lue
 */
@Setter
@Getter
public class AppAccessLog {

    private Long id;
    private String request_url;
    private String request_time;
    private String response_time;
    private Integer c_network;
    private String c_imei;
    private String c_brand;
    private String c_model;
    private String c_os;
    private Integer c_type;
    private String req_msg;
    private String rsp_msg;
    private String c_version;
    private String c_phone;

    
}
