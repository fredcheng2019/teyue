package com.api.bussiness.base;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
public class BaseController {
	
	private static Logger log = LoggerFactory.getLogger(BaseController.class);
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, HttpServletResponse response, ServletRequestDataBinder binder) throws Exception {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	

	public void write(HttpServletResponse rsp,String str){
		try {
			rsp.setHeader("Content-type", "text/html;charset=UTF-8");
			PrintWriter out = rsp.getWriter();
			out.write(str);
			out.flush();
			out.close();
		}catch (Exception e){
			log.error("写错误：",e);
		}
	}
	
	public void writeToJson(HttpServletResponse rsp,Object obj) {
		String json = JSON.toJSONString(obj);
		write(rsp,json);
	}

	public String read(HttpServletRequest request){
		String result=null;
		try {
			byte[] buffer = new byte[5120];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int readSize;
			while ((readSize = request.getInputStream().read(buffer)) >= 0)
				out.write(buffer, 0, readSize);
			result = new String(out.toByteArray(), "UTF-8");
			out.close();
		}catch ( Exception e){
			log.error("读错误：",e);
			result=null;
		}
		return result;
	}
}
