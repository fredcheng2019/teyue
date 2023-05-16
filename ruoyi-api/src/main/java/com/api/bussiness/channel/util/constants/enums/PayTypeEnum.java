package com.api.bussiness.channel.util.constants.enums;

/**
 * 1=支付宝 2=微信 3=网关 4=银联 5=百度钱包 6=京东支付
 */
public enum PayTypeEnum {
    TYPE_ALIPAY("1","支付宝"),
    TYPE_WECHAT("2","微信");
    ;
    PayTypeEnum(String code, String value){
        this.code = code;
        this.value = value;
    }
    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
