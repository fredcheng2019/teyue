package com.api.bussiness.channel.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class XmPayRSAUtils {
    private static final Logger logger = LoggerFactory.getLogger(XmPayRSAUtils.class);

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtkepuv51MpOn4cb2CrDa" +
            "1kD1mdhLnt+xUFl0yUY8S6pD4SukF+cC+c4XMimDDBQilxi05IHc0Xmu5saEGQ8c" +
            "FrFLKa805jM3WnMF91OHImZHa3jjMSEGWUfO+1hObFVgWGSBQUV2eqcpwJtMP2L6" +
            "Ht/WnArcUbnP3W6YGTe3PP7C5OHzUP4RJLKfRs+n7veFTURuDfwpz7ZvBO+o93ie" +
            "6ZBmU1o+PIFiPk0TIsKtPyqBRLPFVxLKfJoMwLASoPeKxORGjsgidFsU24P+S93F" +
            "Tb/qeiX67C3TFtDt9FIkjnrNwPe09wrsp556t4ADvESqll0WLAS6fNLwexvt/jdV" +
            "nwIDAQAB";

    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCktzfK4Zz6IGcw" +
            "p6sROb0B9OJoR6ZTkkZ5LPHQU3zmLnR4YNyPzxufrn1dwfBPpitT3eFWavbTP5cZ" +
            "71dKvlEfJkAlpca8yUAx1STOFOGGRrMz2+Oo1Yf++o2gQdHi2Whu+Wjm3tPyk+3B" +
            "dqgofojgEcyzvA8IVw2NAyb8Oo7xGLbf/GGUyzk8U1p5y8OgxnMPSrhDsFgWHbzM" +
            "x4L7uZzMnYo99YJmfW4+OKgbu+o/TaPsCdePK1g6bX4xMTc1PLKLjtxsT5vSDQD9" +
            "HVv5QR0pNHz0ybmlLdIMJiQcm35VZfa76JaLT8PscPBeoGLSlQg9MW9KeTuxLX2v" +
            "aQEzhLxVAgMBAAECggEAOK99GesO9N2V5Z6uNp1iOHwoNq8BxJ0Ad89J4OH6h9TQ" +
            "7GSZQzyJSyjnqA6IP0D2hERFulHRY93siFZf+i8JB+lIBizI+ktgNkYeBnYVLvrB" +
            "zUUNUhs6VDux5ccBDkq3sAlRWrKwyjiyh/Pn5xe0zzgQ9IHxwnMDDNZa4k7cxoen" +
            "fhARyWE1tZ1/7ghfUjV2mlIy4Fknkg36TTj0Ed0x95Ib1IkDKB5O8lFSH/33hiud" +
            "/Xa8pibuk0dkstCnA6zdBVO9gzj1nrt3lWrGCTsBzkajZ/DkwtJ45YjXqVelt+rj" +
            "cCnQ6F/SVwUhiZugrw19W3pc4k28RHyEnEnC/1YHeQKBgQDQEhvp1qApakt1VsvE" +
            "X4v2fF93a/cWF0tlgmozsVd/gNsLE83c218uRgg3KxO0M35mFofR0/tfIUsUyYRc" +
            "2rI+kt+RNscjOjhzWlfG/cy717DMeII27evoJAtIBwrWLsAeTPWYQ2BqAymlr3RC" +
            "fIFqKStw/6VKGKZJwXz4Qn1f6wKBgQDKqHhuMSNJIZtpa9JUcgEQ/veLLra9KF6m" +
            "+vbvmKRnSvEQPWsj8+9h/cVJfk+L1Pk2yM9LqcH6Y8ahrbcjcomJfWjGVCyXG1kU" +
            "G2DorlmUxGZFvQrG+zAS55vD+T3x3cU/kzvloB7PoSP2aNTKvtcRrw1d+EHPeTy1" +
            "Syx9y0+EvwKBgF/f2EjZgpdSyU5aHi5wMFf1Wy/16baeKTmu7CzBjKXNxCDIkVUx" +
            "KBx0XJi/GqcwCxATVaYT3YIqHySG7pyykOtQVf/dk8jhrN1KRaQFkm8Dpg2xgZ65" +
            "5rfi0+fuhsnGZPQRViFA+o4ewvP7csCoCiHWspOw2ILZ9NWLeIAUcxzdAoGBAMZt" +
            "iGWUVVFgx5PET9ABREwI6gZsed7ibTMgHbLhiW/d8AUPrh0o1qXi3U4lj+uBX3al" +
            "zlruX5E0KqPZvKzHlCV8Fp/CgVp/G4xVdmC9fdbtKpp0C95iymJoRprvKxjsJRsn" +
            "pYkja6euxtCZOmk9zMVy2iq9Tmq/8ZItheWxsS1vAoGAJnLUXJRXPdfMcSXjo25L" +
            "re3ZeNAeZinMEkSONoWXa2Nmm/0tIfynpLNaVwhAWzRXWeJMxlYxc+AkotRJuvJZ" +
            "rNCB5CQjwvvCf7a20OszPlLSEUrAuHDYypRqcWUvSD+FbtwugwmT/jVGUYRVc6lT" +
            "Qd8YO1y4Tm8F4sjgXbwNfO8=";


    /**
     * 用私钥对信息进行数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥-base64加密的
     * @return
     * @throws Exception
     */
    public static String signByPrivateKey(byte[] data, String privateKey) throws Exception {
        logger.info("用私钥对信息进行数字签名");
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = factory.generatePrivate(keySpec);// 生成私钥
        // 用私钥对信息进行数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());

    }

    /**
     * BASE64Encoder 加密
     *
     * @param data 要加密的数据
     * @return 加密后的字符串
     */
    private static String encryptBASE64(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }

    private static byte[] decryptBASE64(String data) {
        return Base64.decodeBase64(data);
    }

    public static boolean verifyByPublicKey(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(decryptBASE64(sign)); // 验证签名
    }
}
