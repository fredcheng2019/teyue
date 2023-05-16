package com.api.base.util;

import org.springframework.util.DigestUtils;

public class MD5 {
    public static String MD5Encode(String signStr) {
        return DigestUtils.md5DigestAsHex(signStr.getBytes());
    }
}
