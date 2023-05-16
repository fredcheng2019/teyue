package com.ruoyi.basedata.domain;

import javax.security.sasl.SaslServer;

public class ConfirmResult {

    private int status;

    private String msg;

    private String withdrawal;

    private String[] balance;

    public String getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(String withdrawal) {
        this.withdrawal = withdrawal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String[] getBalance() {
        return balance;
    }

    public void setBalance(String[] balance) {
        this.balance = balance;
    }
}
