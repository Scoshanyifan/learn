package com.scosyf.learn.net;

import com.alibaba.fastjson.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

public class NetBasic {

	public static void inetAddress() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			byte[] addrBytes = inetAddress.getAddress();
			System.out.println("计算机名：" + inetAddress.getHostName());
			System.out.println("IP地址：" + inetAddress.getHostAddress());
			System.out.println("IP地址字节形式：" + Arrays.toString(addrBytes));
			System.out.println("InetAddress对象：" + inetAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void url() {
		URL baidu = null;
		BufferedReader br = null;
		try {
			baidu = new URL("http://www.baidu.com");
			System.out.println("协议：" + baidu.getProtocol());
			System.out.println("主机：" + baidu.getHost());
			
			br = new BufferedReader(
					new InputStreamReader(
							baidu.openStream(), "utf8"));
			String data = null;
			while ((data = br.readLine()) != null) {
				System.out.println(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			IOUtils.close(br);
		}
	}
	
	public static void main(String[] args) {
		inetAddress();
		url();
	}
}
