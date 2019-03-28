package com.scosyf.learn.basic;

import org.apache.commons.lang3.StringUtils;

public class IfElseTest {
	public static final int COMPLETE = 4;
	public static final int PAYED = 2;
	public static final int PAY_TYPE = 10;

	public static void main(String[] args) {
//		testIfElse();
		testie();
	}
	
	/**
	 * 如果status不是2或4，或者payType小于0，报错
	 */
	public static void testIfElse() {
		int satatus = 2;
		int payType = 10;
		if (!(satatus == COMPLETE || satatus == PAYED || payType > 0)) {
			System.out.println("error");
		}
		
		//错误的写法
		if (satatus != COMPLETE || satatus != PAYED || !(payType > 0)) {
			System.out.println("error");
		}
	}
	
	
	
	public static void testie() {
		String compCode = "ddd";
		String updateAll = null;
		
		if (StringUtils.isNotBlank(compCode) && StringUtils.isBlank(updateAll)) {
			//升级辉腾
			System.out.println("huiteng");
		} else if(StringUtils.isBlank(compCode) && StringUtils.isNotBlank(updateAll)) {
			//升级dtu
			System.out.println("dtu");
		} else {
			System.out.println("error");
		}
	}
	
	
	
}
