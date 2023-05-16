package com.ruoyi.payApi;

/**
 * @Author: Mr.Lu
 * @Date: 2019/5/26 21:02
 */
public class TestRsp {
    // 请求报文
    private String req;
    // 返回报文
    private String rsp;
    // 支付二维码url
    private String qrCodeUrl;

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getRsp() {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

}
