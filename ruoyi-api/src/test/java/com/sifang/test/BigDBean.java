package com.sifang.test;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BigDBean {
	
	private int i;
	
	private BigDecimal b;
	
	private String s;
	
	public static void main(String[] args) {
		/*BigDBean b = new BigDBean();
		b.setB(new BigDecimal("45.01"));
		b.setI(100);
		b.setS("100");
		String msg = JSONObject.fromObject(b).toString();
		System.out.println(msg);
		
		String ss = "{'b':'45.01','s':'100','i':100}";
		
		BigDBean bigDBean = JSON.parseObject(ss, BigDBean.class);
		System.out.println(bigDBean.getB());*/
		
		/*BigDecimal b = new BigDecimal("0.00");
		b.setScale(2);
		BigDecimal bb = new BigDecimal("0.0");
		bb.setScale(2);
		b = bb;
		b.setScale(2);
		System.out.println(b);*/
		
		String str_total_fee = "1.1";
		String[] strs = str_total_fee.split("\\.");
		System.out.println(strs.length);
	}
	

}
