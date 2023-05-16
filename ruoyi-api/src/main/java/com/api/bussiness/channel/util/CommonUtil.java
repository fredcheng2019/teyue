package com.api.bussiness.channel.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 常用工具类
 *
 * @auther Lengff
 * @time 2020/9/5
 * @fileName CommonUtil
 */
public class CommonUtil {
    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 获取待签名串
     *
     * @param params
     * @return
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); ++i) {
            String key = keys.get(i);
            if (params.get(key) == null) {
                continue;
            }
            String value = String.valueOf(params.get(key));
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        log.debug("待签名串:" + prestr);
        return prestr;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static String getCurrTime() {
        return String.valueOf(System.currentTimeMillis());
    }
}
