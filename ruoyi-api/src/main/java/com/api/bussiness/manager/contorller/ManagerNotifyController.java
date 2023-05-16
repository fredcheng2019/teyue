package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.api.base.util.IPUtils;
import com.api.base.util.RequestUtils;
import com.api.base.util.StringUtils;
import com.api.bussiness.manager.service.ManagerNotifyService;
import com.api.bussiness.channel.util.RequestStreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bussiness.base.BaseController;
import com.api.bussiness.dao.table.finance.FinancialSetting;
import com.api.bussiness.merchant.service.WhiteListIpService;

import java.util.Map;

@RestController
public class ManagerNotifyController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ManagerNotifyController.class);

    @Autowired
    ManagerNotifyService managerNotifyService;

    @Autowired
    WhiteListIpService whiteListIpService;

    @RequestMapping(value = "/notify/{type}/{className}")
    public void notifyTrade(HttpServletRequest req, HttpServletResponse rsp, @PathVariable("type") String type, @PathVariable("className") String className) {
        //判断是否开启IP拦截
        FinancialSetting financialSetting = whiteListIpService.getFinancialSetting();
        String reqIp = IPUtils.getIpAddr(req);
        if (financialSetting.getChannel_white() == FinancialSetting.STATUS_OPEN) {
            //IP拦截

            boolean ipResult = whiteListIpService.getChannelWhiteListCountByIp(reqIp);
            if (!ipResult) {
                log.info(reqIp+"Illegal ip");
                write(rsp, "Illegal ip");
                return;
            }
        }
        String reqUrlName = req.getServletPath();
        // 先获取post方式的获取参数格式
        Map<String, Object> queryParams = RequestUtils.getQueryParams(req);
        String msg = JSON.toJSONString(queryParams);
        log.info(reqIp+"POST格式数据:{}", msg);
        if (StringUtils.isBlank(msg) || "{}".equals(msg)) {
            // 如果上述方式无法获取参数,就使用json的格式获取参数试试
            Map<String, String> map = RequestStreamUtil.getparams(req);
            msg = JSON.toJSONString(map);
            log.info("json格式数据:{}", msg);
        }
        log.info("请求地址:{}", reqUrlName);
        String rspMsg = "";
        //先判断类名是否存在
        boolean existClassName = managerNotifyService.existClassName(className);
        if (!existClassName) {
            write(rsp, "Illegal Request");
            return;
        }
        if (type.equals("pay")) {
            try {
                rspMsg = managerNotifyService.parsePayNotify(msg, className);
            } catch (Exception e) {
                log.error(e.getMessage());
                rspMsg = "system error";
            }
            write(rsp, rspMsg);
        } else if (type.equals("withdrawal")) {
            try {
                rspMsg = managerNotifyService.parseWithDrawalNotify(msg, className);
            } catch (Exception e) {
                log.error(e.getMessage());
                rspMsg = "system error";
            }
            write(rsp, rspMsg);
        } else {
            write(rsp, "Illegal Request");
            return;
        }
    }
}
