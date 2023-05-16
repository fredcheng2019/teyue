package com.sifang.test;

import java.math.BigDecimal;

public class DTest {
	
	public static void main(String[] args) {
		
		BigDecimal fee = new BigDecimal("100");
		BigDecimal f = new BigDecimal("10.06");
		BigDecimal ff = fee.subtract(f);
		System.out.println(ff.toString());
	}

}
