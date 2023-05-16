package com.api.bussiness.manager.contorller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.bussiness.manager.service.ManagerWithdrawalApplyingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.api.bussiness.base.BaseController;
import com.api.bussiness.manager.bean.ManagerReqWithdrawalApplying;
import com.api.bussiness.manager.bean.ManagerRspWithdrawalApplying;


@RestController
public class ManagerWithdrawalApplyingController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ManagerWithdrawalApplyingController.class);
	
	@Autowired
    ManagerWithdrawalApplyingService managerWithdrawalApplyingService;
	
	@RequestMapping(value = "/manager/sureApply")
	public void withdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
			String msg = (String)request.getAttribute("msg");
			ManagerReqWithdrawalApplying managerReqWithdrawalApplying = JSON.parseObject(msg, ManagerReqWithdrawalApplying.class);
			
			ManagerRspWithdrawalApplying managerRspWithdrawalApplying = null;
			if(managerReqWithdrawalApplying.getType() == ManagerReqWithdrawalApplying.MANAGER_REQ_WITHDRAWAL_APPLYING_PASS) {
				managerRspWithdrawalApplying = managerWithdrawalApplyingService.withdrawalPass(managerReqWithdrawalApplying);
			}else if(managerReqWithdrawalApplying.getType() == ManagerReqWithdrawalApplying.MANAGER_REQ_WITHDRAWAL_APPLYING_REFUSE) {
				managerRspWithdrawalApplying = managerWithdrawalApplyingService.withdrawalRefuse(managerReqWithdrawalApplying);
			}
			this.writeToJson(response, managerRspWithdrawalApplying);
		}catch (Exception e) {
			log.error(e.getMessage());
			ManagerRspWithdrawalApplying managerRspWithdrawalApplying = new ManagerRspWithdrawalApplying();
			managerRspWithdrawalApplying.setMsg("操作失败");
			managerRspWithdrawalApplying.setStatus(ManagerRspWithdrawalApplying.MANAGER_RSP_WITHDRAWAL_APPLY_FAIL);
			this.writeToJson(response, managerRspWithdrawalApplying);
		}
	}

}
