package com.api.interceptor;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.base.util.FileUtil;
import com.api.base.util.IPUtils;
import com.api.base.util.ReqCheckUtil;
import com.api.base.util.SignUtil;
import com.api.bussiness.merchant.service.RequestService;
import com.api.bussiness.merchant.service.WhiteListIpService;
import com.api.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.bussiness.dao.table.finance.FinancialSetting;
import com.api.bussiness.merchant.bean.MerchantReqCheckPay;
import com.api.bussiness.merchant.bean.MerchantReqCheckWithdrawal;
import com.api.bussiness.merchant.bean.MerchantReqPay;
import com.api.bussiness.merchant.bean.MerchantReqWithdrawal;
import com.api.bussiness.merchant.bean.MerchantRspErrorResult;

import net.sf.json.JSONObject;

@Component
public class MerchantInterceptor extends BaseInterceptor implements HandlerInterceptor {  //实现原生拦截器的接口

	private static Logger log = LoggerFactory.getLogger(ManagerInterceptor.class);

	@Autowired
    RequestService requestService;

	@Autowired
    WhiteListIpService whiteListIpService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

    	request.setCharacterEncoding("utf-8");
		String reqUrlName = request.getServletPath().toString();
		log.info("接口地址：{}",reqUrlName);
		if(request.getServletPath().toString().contains("error")) {
			writeErrorRsp(response, ErrorCode.SYSTEM_ABNORMAL);
			return false;
		}

		String reqIp = IPUtils.getIpAddr(request);
		//判断是否开启IP拦截
		FinancialSetting financialSetting = whiteListIpService.getFinancialSetting();
		if(financialSetting.getMerchant_white() == FinancialSetting.STATUS_OPEN) {
			//IP拦截
			boolean ipResult = whiteListIpService.getMerchantWhiteListCountByIp(reqIp);
			log.info("访问IP：{},是否通过:{}",reqIp,ipResult);
			if(!ipResult) {
				writeErrorRsp(response,ErrorCode.UNKNOW_IP);
				return false;
			}
		}

		//签名校验
		String reqStr = this.readReqStr(request);

		Map<String,Object> reqMap = SignUtil.getMapFromStr(reqStr);
		log.info("访问IP：{},请求报文:{}",reqIp,reqMap);
		if(null == reqMap) {
			writeErrorRsp(response,ErrorCode.ILLEGAL_PARAMETER);
			return false;
		}
		String key = requestService.getMerchantKeyByCode(reqMap.get("mch_id")+"");
		if(key == null) {
			key = System.currentTimeMillis()+"";
		}
		String sign = SignUtil.getSignReq(reqStr, key);

		if(sign.equals(reqMap.get("sign"))) {
			log.info("签名校验成功");
			if(reqUrlName.equals("/pay/order")) {//下单接口
				//所需字段进行判断
				String result = ReqCheckUtil.checkPlaceAnOrder(reqMap);
				if(result.equals(ErrorCode.SUCCESS)) {
					MerchantReqPay req = ReqCheckUtil.mapToBean(reqMap, MerchantReqPay.class);
					req.setSecret_key(key);
					request.setAttribute("msg", req);
				}else {
					writeErrorRsp(response,result);
					return false;
				}
			}else if(reqUrlName.equals("/pay/orderQuery")){//订单查询接口
				//所需字段进行判断
				String result = ReqCheckUtil.checkOrder(reqMap);
				if(result.equals(ErrorCode.SUCCESS)) {
					MerchantReqCheckPay req = ReqCheckUtil.mapToBean(reqMap, MerchantReqCheckPay.class);
					req.setSecret_key(key);
					request.setAttribute("msg", req);
				}else {
					writeErrorRsp(response,result);
					return false;
				}
			}else if(reqUrlName.equals("/pay/withdrawal")){//代付接口
				//所需字段进行判断
				String result = ReqCheckUtil.checkWithdrawal(reqMap);
				if(result.equals(ErrorCode.SUCCESS)) {
					MerchantReqWithdrawal req = ReqCheckUtil.mapToBean(reqMap, MerchantReqWithdrawal.class);
					req.setSecret_key(key);
					request.setAttribute("msg", req);
				}else {
					writeErrorRsp(response,result);
					return false;
				}
			}else if(reqUrlName.equals("/pay/withdrawalCheck")){//代付查询接口
				//所需字段进行判断
				String result = ReqCheckUtil.checkWithDrawalInfo(reqMap);
				if(result.equals(ErrorCode.SUCCESS)) {
					MerchantReqCheckWithdrawal req = ReqCheckUtil.mapToBean(reqMap, MerchantReqCheckWithdrawal.class);
					req.setSecret_key(key);
					request.setAttribute("msg", req);
				}else {
					writeErrorRsp(response,result);
					return false;
				}
			}
			return true;
		}else {
			log.info("签名校验失败");
			writeErrorRsp(response,ErrorCode.ERROR_SIGN);
			return false;
		}
    }

    private String readReqStr(HttpServletRequest request) {
		String reqJson = null;
		try {
			request.setCharacterEncoding("UTF-8");
			//取消json报文字节流加密
			byte[] reqByte = FileUtil.streamToBytes(request.getInputStream());
			log.info("请求的报文的大小-->"+reqByte.length, this.getClass().getName());
			reqJson = new String(reqByte, "UTF-8");
			log.info("请求的JSON：\\n"+reqJson, this.getClass().getName());
		} catch (Exception e) {
			reqJson = null;
			log.error(e.getMessage());
		}
		return reqJson;
	}

	public void writeErrorRsp(HttpServletResponse rsp,String str) {
		try {
			rsp.setHeader("Content-type", "text/html;charset=UTF-8");
			MerchantRspErrorResult rspErr = new MerchantRspErrorResult();
			rspErr.setCode("error");
			rspErr.setMsg(str);
			String msg = JSONObject.fromObject(rspErr).toString();
			PrintWriter out = rsp.getWriter();
			out.write(msg);
			out.flush();
			out.close();
			out = null;
			return;
		} catch (Exception e) {
			log.info(this.getClass().getName(), e);
		}
	}
}
