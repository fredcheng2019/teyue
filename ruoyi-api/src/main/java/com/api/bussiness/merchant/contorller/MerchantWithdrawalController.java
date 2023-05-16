package com.api.bussiness.merchant.contorller;

import com.api.bussiness.merchant.bean.MerchantReqWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import com.api.bussiness.merchant.bean.MerchantRspWithdrawal;
import com.api.bussiness.merchant.service.MerchantWithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bussiness.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 代付接口
 * @author 首信易支付
 *
 */
@RestController
public class MerchantWithdrawalController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(MerchantWithdrawalController.class);

	@Autowired
    MerchantWithdrawalService merchantWithdrawalService;

	@RequestMapping(value = "/pay/withdrawal")
	public void withdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
			MerchantReqWithdrawal merchantReqWithdrawal = (MerchantReqWithdrawal) request.getAttribute("msg");

			MerchantRspWithdrawal merchantRspWithdrawal = merchantWithdrawalService.withdrawal(merchantReqWithdrawal);

			if (merchantRspWithdrawal.getCode().equals("success")) {
				writeToJson(response, merchantRspWithdrawal);
			} else {
				MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
				merchantRspErrorResult.setCode("error");
				merchantRspErrorResult.setMsg(merchantRspWithdrawal.getMsg());
				writeToJson(response, merchantRspErrorResult);
			}
		} catch (Exception e) {
			log.info("代付异常:{}",e.getMessage());
			log.error("代付异常", e);
			e.printStackTrace();
			MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
			merchantRspErrorResult.setCode("error");
			merchantRspErrorResult.setMsg("代付异常");
			writeToJson(response, merchantRspErrorResult);
		}
	}
}
