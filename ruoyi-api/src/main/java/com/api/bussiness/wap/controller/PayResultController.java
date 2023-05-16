package com.api.bussiness.wap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pay_result")
public class PayResultController {
	
	private static Logger log = LoggerFactory.getLogger(PayResultController.class);
	
	@RequestMapping("/pay_result")
	public String index(Model model){
		log.info("支付成功跳转");
        return "pay/pay_result";
    }

}
