package com.scosyf.learn.net.IO;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.alibaba.fastjson.util.IOUtils;

/**
 * https://www.imooc.com/article/16892
 * @author scosyf
 *
 */
public class UDPServer {

	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		try {
			//1.创建连接管理
			datagramSocket = new DatagramSocket(9999);
			//2.创建数据报
			byte[] data = new byte[1024];
			DatagramPacket packetFrom = new DatagramPacket(data, data.length);
			
			System.out.println("服务端启动...");
			//3.阻塞接收客户端数据报
			datagramSocket.receive(packetFrom);
			//4.读取数据
			String info = new String(data, 0, packetFrom.getLength());
			System.out.println("服务端收到消息：" + info);
			//5.回复客户端，同样是数据报形式
			InetAddress clientAddr = packetFrom.getAddress();
			int clientPort = packetFrom.getPort();
			byte[] response = "服务端收到消息了".getBytes("utf8");
			DatagramPacket packetTo = new DatagramPacket(response, response.length, clientAddr, clientPort);
			datagramSocket.send(packetTo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(datagramSocket);
		}
	}
	
}
