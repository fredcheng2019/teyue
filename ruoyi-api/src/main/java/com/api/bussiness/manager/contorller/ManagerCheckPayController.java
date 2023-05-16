package com.api.bussiness.manager.contorller;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.manager.bean.ManagerReqCheckPay;
import com.api.bussiness.manager.bean.ManagerRspCheckPay;
import com.api.bussiness.manager.service.ManagerCheckPayService;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.channel.context.ChannelRspCheckPay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ManagerCheckPayController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ManagerCheckPayController.class);
	
	@Autowired
    ManagerCheckPayService managerCheckPayService;
	
	@RequestMapping(value = "/manager/checkPay")
	public void checkPay(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
            String msg = (String)request.getAttribute("msg");
            ManagerReqCheckPay managerReqCheckPay = JSON.parseObject(msg, ManagerReqCheckPay.class);
			ManagerRspCheckPay managerRspCheckPay = managerCheckPayService.checkPay(managerReqCheckPay);
			writeToJson(response, managerRspCheckPay);
		}catch (Exception e) {
			log.error(e.getMessage());
			ChannelRspCheckPay channelRspCheckPay = new ChannelRspCheckPay();
			channelRspCheckPay.setStatus(ChannelRspCheckPay.RSP_STATUS_CHECKPAY_OTHER);
			channelRspCheckPay.setMsg("系统异常");
			writeToJson(response, channelRspCheckPay);
		}
		
	}

}
