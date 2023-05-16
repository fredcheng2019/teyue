package com.api.bussiness.merchant.contorller;

import com.api.bussiness.merchant.bean.MerchantReqCheckWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspCheckWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import com.api.bussiness.merchant.service.MerchantCheckWithDrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bussiness.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 代付查询
 * @author 首信易支付
 *
 */
@RestController
public class MerchantCheckWithdrawalController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(MerchantCheckWithdrawalController.class);

	@Autowired
    MerchantCheckWithDrawalService merchantCheckWithDrawalService;

	@RequestMapping(value = "/pay/withdrawalCheck")
	public void withdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("Content-Type", "text/json;charset=UTF-8");
		try {
			MerchantReqCheckWithdrawal merchantReqCheckWithdrawal = (MerchantReqCheckWithdrawal)request.getAttribute("msg");
			MerchantRspCheckWithdrawal merchantRspCheckWithdrawal = merchantCheckWithDrawalService.checkWithdrawal(merchantReqCheckWithdrawal);

			if(merchantRspCheckWithdrawal.getCode().equals("success")) {
				writeToJson(response,merchantRspCheckWithdrawal);
			}else {
				MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
				merchantRspErrorResult.setCode("error");
				merchantRspErrorResult.setMsg(merchantRspCheckWithdrawal.getMsg());
				writeToJson(response,merchantRspErrorResult);
			}
		}catch (Exception e) {
			log.error("查询代付异常",e);
			MerchantRspErrorResult merchantRspErrorResult = new MerchantRspErrorResult();
			merchantRspErrorResult.setCode("error");
			merchantRspErrorResult.setMsg("查询代付异常");
			writeToJson(response,merchantRspErrorResult);
		}
	}
}
