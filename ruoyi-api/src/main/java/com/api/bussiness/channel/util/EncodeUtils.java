package com.api.bussiness.channel.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;

public class EncodeUtils {
    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    public EncodeUtils() {
    }

    public static String hexEncode(byte[] input) {
        return Hex.encodeHexString(input);
    }

    public static byte[] hexDecode(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException var2) {
            throw new IllegalStateException("Hex Decoder exception", var2);
        }
    }

    public static String base64Encode(byte[] input) {
        return new String(Base64.encodeBase64(input));
    }

    public static String base64UrlSafeEncode(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    public static byte[] base64Decode(String input) {
        return Base64.decodeBase64(input);
    }

    public static String urlEncode(String input) {
        return urlEncode(input, "UTF-8");
    }

    public static String urlEncode(String input, String encoding) {
        try {
            return URLEncoder.encode(input, encoding);
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", var3);
        }
    }

    public static String urlDecode(String input) {
        return urlDecode(input, "UTF-8");
    }

    public static String urlDecode(String input, String encoding) {
        try {
            return URLDecoder.decode(input, encoding);
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", var3);
        }
    }

    public static String htmlEscape(String html) {
        return StringEscapeUtils.escapeHtml(html);
    }

    public static String htmlUnescape(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml(htmlEscaped);
    }

    public static String xmlEscape(String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    public static String xmlUnescape(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    public static String testDigest(String myinfo) {
        byte[] digesta = null;

        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(myinfo.getBytes("gbk"));
            digesta = alga.digest();
        } catch (UnsupportedEncodingException var3) {
            System.out.println("-----------------------------------------------");
            System.out.println("MD5发生错误！");
            System.out.println("-----------------------------------------------");
        } catch (NoSuchAlgorithmException var4) {
            System.out.println("-----------------------------------------------");
            System.out.println("MD5发生错误！");
            System.out.println("-----------------------------------------------");
        }

        return byte2hex(digesta);
    }

    public static String testDigest(String myinfo, String encode) throws UnsupportedEncodingException {
        byte[] digesta = null;

        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(myinfo.getBytes(encode));
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException var4) {
            System.out.println("MD5发生错误！");
        }

        return byte2hex(digesta);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }

            int var10000 = b.length;
        }

        return hs;
    }

    public static String genRandomNum(int pwd_len) {
        //int maxNum = true;
        int count = 0;
        char[] str = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());

        while(count < pwd_len) {
            int i = Math.abs(r.nextInt(10));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                ++count;
            }
        }

        return pwd.toString();
    }
}
