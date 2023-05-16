package com.api.bussiness.wap.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.api.bussiness.wap.bean.JumpBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jump")
public class JumpController {

	private static Logger log = LoggerFactory.getLogger(PayResultController.class);

	@RequestMapping("/pay")
	public String index(HttpServletRequest request){

		log.info("支付跳转");

		Map<String,String> map = showParams(request);
		String req_url = map.remove("req_url");
		//获取请求地址
		request.setAttribute("req_url", req_url);

		//获取请求参数
		List<JumpBean> reqList = getReqList(map);

		request.setAttribute("reqList", reqList);

        return "jump/pay";
    }

	//获取请求参数
	private static List<JumpBean> getReqList(Map<String,String> map){
		map.remove("req_url");
		List<JumpBean> reqList = new ArrayList<JumpBean>();
		for (String key : map.keySet()) {
			String value = map.get(key);
			JumpBean jumpBean = new JumpBean();
			jumpBean.setKey(key);
			jumpBean.setValue(value);
			reqList.add(jumpBean);
		}
		return reqList;
	}


	//从request获取请求参数
	@SuppressWarnings("rawtypes")
	private static Map<String,String> showParams(HttpServletRequest request) {
        Map<String,String> map = new HashMap<String,String>();
        Enumeration paramNames = request.getParameterNames();
        log.info("参数个数：{}",request.getParameterMap().size());
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            log.info("参数名：{}",paramName);
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length >0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        Set<Map.Entry<String, String>> set = map.entrySet();
        log.debug("==============================================================");
        for (Map.Entry entry : set) {
        	log.info(entry.getKey() + ":" + entry.getValue());
        }
        log.debug("=============================================================");
        return map;
    }


}
