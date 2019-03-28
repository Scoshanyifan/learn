package com.scosyf.learn.basic;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathTest {
	
	/**
	 * 进制问题
	 */
	public static void radixTest() {
		String times = new BigInteger("C0C00F", 16).toString(2);
		System.out.println(times);
		char[] crs = times.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = crs.length - 1; i >= 0; i--) {
			System.out.println(crs[i] + " >>> " + (crs[i] == 1));
			if (crs[i] == '1') {
				System.out.println("hour : " + (crs.length - i - 1));
				sb.append(crs.length - i - 1).append("h ");
			}
		}
		System.out.println(sb.toString());
	}
	
	/**
	 * 直接用浮点数进行运算会有精度问题
	 * 
	 * 通过字符串传入，BigDemical(String val)
	 */
	public static void floatPointTest() {
		//这里以float为例说明浮点数精度问题，double同理，因为位数太多就不演示了
		float f1 = 1.0f;
		float f2 = 0.33f;
		float f3 = 0.44f;
		//0.66999996
		System.out.println(f1 - f2);	
		//1.0 - 0.44为什么是准确的?
		System.out.println(f1 - f3);
		
		//正确做法是通过BigDecimal的String作为参数，来运算
		BigDecimal b1 = new BigDecimal(Float.toString(f1));
		BigDecimal b2 = new BigDecimal(Float.toString(f2));
		System.out.println("BigDecimal处理结果：" + b1.subtract(b2));
		
		float f = 2019021314;
		double d = 2019021314;
		System.out.println("float:" + f);
		//这里可以看出double的是精确的，原因是float的尾数只有23位，不够用了
		System.out.println("double: " + d);
		//float打印出31位，省略符号位0：[0] 1001 1101  1110 0001 0101 1111 0100 100
		System.out.println(Integer.toBinaryString(Float.floatToIntBits(f)));
		System.out.println(Long.toBinaryString(Double.doubleToLongBits(d)));
		
	}
	
	/**
	 * 四舍五入问题
	 */
	public static void mathTest() {
		Double num = (double) 17665 / 3600;
		System.out.println(num + ", " + Math.round(num));
	}

	public static void main(String[] args) {
		
		floatPointTest();
	}
}
