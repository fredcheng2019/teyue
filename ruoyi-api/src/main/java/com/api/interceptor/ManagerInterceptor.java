package com.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.base.config.SystemProperties;
import com.api.base.util.IPUtils;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import com.api.bussiness.merchant.service.RequestService;
import com.api.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ManagerInterceptor extends BaseInterceptor implements HandlerInterceptor {  //实现原生拦截器的接口

    private static Logger log = LoggerFactory.getLogger(ManagerInterceptor.class);

    @Autowired
    RequestService requestService;

    @Autowired
    SystemProperties systemProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setCharacterEncoding("utf-8");
        String reqUrlName = request.getServletPath().toString();
        log.info("接口地址-{}", reqUrlName);
        if (request.getServletPath().toString().contains("error")) {
            MerchantRspErrorResult rspErr = new MerchantRspErrorResult();
            rspErr.setCode("error");
            rspErr.setMsg(ErrorCode.SYSTEM_ABNORMAL);
            write(response,rspErr);
            return false;
        }

        //黑名单拦截
        String reqIp = IPUtils.getIpAddr(request);
        log.info("请求IP：{}", reqIp);
        String[] whiteList = systemProperties.getPlatformWhiteList().split(",");
        if (null != whiteList && whiteList.length == 0) {
            for (int i = 0; i < whiteList.length; i++) {
                if (whiteList[i].equals(reqIp)) {
                    log.info("非法IP：{}", reqIp);
                    return  false;
                }
            }
        }

        String reqStr = read (request);
        request.setAttribute("msg", reqStr);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
