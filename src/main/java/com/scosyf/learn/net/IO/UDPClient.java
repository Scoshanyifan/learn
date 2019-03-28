package com.scosyf.learn.net.IO;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.alibaba.fastjson.util.IOUtils;

public class UDPClient {

	public static void main(String[] args) {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
			//客户端发消息
			byte[] data = "请求报到".getBytes("utf8");
			InetAddress serverAddr = InetAddress.getByName("localhost");
			int port = 9999;
			DatagramPacket packetTo = new DatagramPacket(data, data.length, serverAddr, port);
			clientSocket.send(packetTo);
			//客户端接收响应
			byte[] response = new byte[1024];
			DatagramPacket packageFrom = new DatagramPacket(response, response.length, serverAddr, port);
			clientSocket.receive(packageFrom);
			String reponseInfo = new String(response, 0, packageFrom.getLength());
			System.out.println("服务端相应：" + reponseInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(clientSocket);
		}
	}
}
