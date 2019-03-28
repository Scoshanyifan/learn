package com.scosyf.learn.net.IO;

import com.alibaba.fastjson.util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 传统IO模型
 *
 * @author kunbu
 * @time 2019/2/20 10:49
 **/
public class SocketClient {
	
	public static void main(String[] args) {
		client("localhost", 8888);
	}

	public static void client(String host, int port) {
		Socket socket = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			for (int i = 0; i < 5; i++) {
				Thread.sleep(1000);
				//1.创建连接
				socket = new Socket(host, port);
				//2.获取输出流
				pw = new PrintWriter(socket.getOutputStream());
				pw.write("这里是客户端：" + new Date());
				//3.输出信息
				pw.flush();

				//4.关闭输出流
//				socket.shutdownOutput();
//				//5.打开输入流并接收响应
//				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf8"));
//				StringBuilder response = new StringBuilder();
//				String data = null;
//				while ((data = br.readLine()) != null) {
//					response.append(data);
//				}
//				System.out.println("服务端回应：" + response.toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(pw);
			IOUtils.close(br);
			IOUtils.close(socket);
		}
	}
	
}
