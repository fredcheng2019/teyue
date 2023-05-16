package com.api.bussiness.merchant.contorller;

import com.api.bussiness.merchant.bean.MerchantRspErrorResult;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.bussiness.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 模拟渠道回调
 * @author 首信易支付
 *
 */
@RestController
public class MerchantSimulateNotifyWithdrawalController extends BaseController {
	private static int RSP_TYPE =1;
	private static Logger log = LoggerFactory.getLogger(MerchantSimulateNotifyWithdrawalController.class);

	@RequestMapping(value = "/merchant/simulateNotifyWithdrawal")
	public void merchantSimulateNotifyWithdrawal(HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json;charset=UTF-8");
		try {
			String reqStr =read(request);
			log.info("请求的报文：{}", reqStr+"");
			String rspStr = "";
			if(RSP_TYPE == 0) {
				rspStr = "success";
			}else if(RSP_TYPE == 1) {
				MerchantRspErrorResult rspErr = new MerchantRspErrorResult();
				rspErr.setCode("success");
				rspErr.setMsg("成功");
				rspStr = JSONObject.fromObject(rspErr).toString();
			}else if(RSP_TYPE == 2) {
				StringBuilder sb = new StringBuilder();
				sb.append("code=success&");
				sb.append("msg=成功");
				rspStr = sb.toString();
			}
			RSP_TYPE ++;
			if(RSP_TYPE == 3) {
				RSP_TYPE = 0;
			}
			log.info("响应报文：{}",rspStr);
			write(response,rspStr);
			return;
		}catch (Exception e) {
			log.error("商户回调异常", e);
		}
	}

}
