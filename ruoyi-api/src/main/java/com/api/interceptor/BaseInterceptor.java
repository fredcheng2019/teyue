package com.api.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONObject;

public class BaseInterceptor {
	
	private static Logger log = LoggerFactory.getLogger(BaseInterceptor.class);

	public String read(HttpServletRequest request) {
		String reqJson = null;
		try {
			request.setCharacterEncoding("UTF-8");
			byte[] reqByte = streamToBytes(request.getInputStream());
			reqJson = new String(reqByte, "UTF-8");
			log.info("拦截器接收 - {}" + reqJson);
		} catch (Exception e) {
			reqJson = null;
			log.error(e.getMessage());
		}
		return reqJson;
	}

	public void write(HttpServletResponse rsp, Object obj) {
		rsp.setCharacterEncoding("UTF-8");
		rsp.setContentType("text/*;charset=UTF-8");
		try {
			String rspJson = JSONObject.fromObject(obj).toString();
			log.info("拦截器返回 - {}", rspJson);
			PrintWriter dout = rsp.getWriter();
			dout.write(rspJson);
			dout.close();
			return;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public byte[] streamToBytes(InputStream in) throws IOException {
		byte[] buffer = new byte[5120];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int readSize;
		while ((readSize = in.read(buffer)) >= 0)
			out.write(buffer, 0, readSize);
		byte[] result =  out.toByteArray();
		out.close();
		return result;
	}
}
