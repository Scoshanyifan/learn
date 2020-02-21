package com.scosyf.learn.string;

import java.util.Arrays;

/**
 * 1. String的不可变性体现在内部char[]是final的，即字符串内容不可变，初始化时确定，并且不提供修改方法，如果调用replace等方法直接返新的String
 * 	  PS：可以利用反射特性破坏String的不可变性 https://segmentfault.com/a/1190000019865846
 *
 * 2. final String是保证String不可被继承
 *
 * @author kunbu
 **/
public class StringTest {
	
	public static final String TEXT = "林内温馨提示：感谢使用林内E86系列热水器，您的热水器已使用超过%1$s年，"
			+ "根据《家用燃气燃烧器具安全管理规程》规定，燃气热水器使用时间到达%1$s年，建议更换。"
			+ "目前林内已推出多款新品燃气热水器，详询各大商场或电商平台。";

	public static void testIndexOf() {
		String origin = "org.apache.catalina.session.StandardSessionFacade@7147820e;admin;aGFkbGlua3MuY29t;";
		String old = "编辑角色（必传参数:'id','roleName','roleMenuFunctionList'）";
		String str = "null',roleName='领航订单客服'}menuId=";
		String splitterStr = "A0BC92D94BD82755F807CBC1B78A0AFF;865533033393442;null;0865533033110341;";
		
		int ftIdx = origin.indexOf(";");
		int secIdx = origin.indexOf(";", ftIdx + 1);
		System.out.println(origin.substring(ftIdx + 1, secIdx));
		
		if (old.indexOf("（") > 0) {
			System.out.println(old.substring(0, old.indexOf("（")));
		}
		
		String rn = "roleName";
		int rnIndex = str.indexOf(rn);
		int lastIndex = str.indexOf("'", rnIndex + rn.length() + 2);
		String roleName = str.substring(rnIndex + rn.length() + 2, lastIndex);
		System.out.println(roleName);
		
		String params[] = splitterStr.split(";");
		System.out.println(Arrays.toString(params));
		System.out.println(params.length);
	}
	
	private static String subStringContent(String target, String origin) {
		int beginIndex = origin.indexOf(target);
		if (beginIndex > 0) {
			int lastIndex = origin.indexOf("'", beginIndex + target.length() + 2);
			String result = origin.substring(beginIndex + target.length() + 2, lastIndex);
			return result;
		}
		return "";
	}
	
	private static void testStringFormat() {
		String reminder = String.format(TEXT, 3);
		System.out.println(reminder);
	}
	
	public static void main(String[] args) {
		testStringFormat();
		
		testIndexOf();
		System.out.println(subStringContent("roleName", "null',roleName='领航订单客服'}menuId="));
		
		
		String lowerStr = "1z&u2";
		System.out.println("upper: " + lowerStr.toUpperCase());
	}
	
}
