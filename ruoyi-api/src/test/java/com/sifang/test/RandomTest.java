package com.sifang.test;

import java.util.Random;

public class RandomTest {
	
	
	public static void main(String[] args) {
		Random random = new Random();              
        //产生随机数
        int number = random.nextInt(4 - 0 + 1) + 0;
        System.out.println(number);
	}

}
