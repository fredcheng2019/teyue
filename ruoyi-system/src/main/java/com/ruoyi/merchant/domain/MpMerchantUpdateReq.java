package com.ruoyi.merchant.domain;

import com.ruoyi.merchantAccount.domain.MerchantSetting;
import com.ruoyi.system.domain.SysUser;

/**
 * 表 mp_merchant
 *
 * @author ruoyi
 * @date 2019-05-06
 */
public class MpMerchantUpdateReq extends MerchantSetting {
    private static final long serialVersionUID = 1L;

    private String userName;

    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private String phonenumber;

    private String sex;


}
