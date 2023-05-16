package com.sifang.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
	
	public static void main(String[] args) throws ParseException {
		/*SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
		Date date = sdf.parse("2019-06-09+12%3A16%3A48");
		String str = sdf.format(date);
		System.out.println(str);*/
		
		String str = "2019-06-09+12%3A16%3A48";
		
		System.out.println(str.replace("+", " ").replaceAll("%3A", ":"));
	}

}
