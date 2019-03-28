package com.scosyf.learn.net.IO;

import com.alibaba.fastjson.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统IO模型
 *
 * 问题：
 * 1.一个线程只管理一个连接
 * 2.线程一直阻塞等待数据
 * 3.读取数据面向流
 *
 * @author kunbu
 * @time 2019/2/20 10:49
 **/
public class SocketServer {
	
	public static void main(String[] args) {
		server(8888);
	}

	public static void server(int port) {
		try {
			//1.创建服务端连接管理
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("服务端启动，等待客户端连接...");

			new Thread(() -> {
				while (true) {
					try {
						//2.阻塞方式监听可能的连接
						Socket socket = serverSocket.accept();
						System.out.println("客户端信息：" + socket.getInetAddress());

						//3.新建线程处理连接
						new Thread(() -> {
							BufferedReader br = null;
							PrintWriter pw = null;
							try {
								//4.获取输入流，然后打印
								br = new BufferedReader(
										new InputStreamReader(
												socket.getInputStream(), "utf8"));
								StringBuilder info = new StringBuilder();
								String data = null;
								while ((data = br.readLine()) != null) {
									info.append(data);
								}
								System.out.println("服务端接收到的消息：" + info.toString());
								//5.关闭输入流
								socket.shutdownInput();
								//6.打开输出流进行应答
								pw = new PrintWriter(socket.getOutputStream());
								pw.write("服务端已收到消息...");
								pw.flush();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								IOUtils.close(br);
								IOUtils.close(pw);
								IOUtils.close(socket);
							}
						}).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
