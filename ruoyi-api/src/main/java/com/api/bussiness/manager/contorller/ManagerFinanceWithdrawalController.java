package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerFinanceWithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqFinanceWithdrawal;
import com.api.bussiness.manager.bean.ManagerRspFinanceWithdrawal;


@RestController
public class ManagerFinanceWithdrawalController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
    ManagerFinanceWithdrawalService managerFinanceWithdrawalService;

	
	/**
	 * 平台财务代付
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/manager/financeWithdrawal")
	public void financeWithdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
            String msg = (String)request.getAttribute("msg");
            ManagerReqFinanceWithdrawal managerReqFinanceWithdrawal = JSON.parseObject(msg, ManagerReqFinanceWithdrawal.class);
            ManagerRspFinanceWithdrawal managerRspFinanceWithdrawal = managerFinanceWithdrawalService.managerFinanceWithdrawal(managerReqFinanceWithdrawal);
            writeToJson(response, managerRspFinanceWithdrawal);
		}catch (Exception e) {
			log.error(e.getMessage());
			ManagerRspFinanceWithdrawal managerRspFinanceWithdrawal = new ManagerRspFinanceWithdrawal();
			managerRspFinanceWithdrawal.setStatus(ManagerRspFinanceWithdrawal.RSP_STATUS_FAIL);
			managerRspFinanceWithdrawal.setMsg("系统异常");
			writeToJson(response, managerRspFinanceWithdrawal);
		}
	}

}
