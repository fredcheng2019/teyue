package com.ruoyi.withdrawalParam.domain;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/23 21:36
 */
public class WithdrawalRsp {
    // 代码  success   error
    String code;
    // 提示信息
    String msg;
    // 平台商户号
    String mch_id;
    // 批号   成功有返回
    String fbatchno;
    // 签名
    String sign;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getFbatchno() {
        return fbatchno;
    }

    public void setFbatchno(String fbatchno) {
        this.fbatchno = fbatchno;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
