package com.api.bussiness.channel.util.constants.enums;

public enum  DeviceEnum {
    MOBILE("1","移动"),
    PC("2","PC"),
    ;
    DeviceEnum(String code, String value){
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
