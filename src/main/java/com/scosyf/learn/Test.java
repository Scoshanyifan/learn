package com.scosyf.learn;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Test {

    public static byte parseHexString(String s){
        int i = Integer.parseInt(s, 16);
        return (byte)i;
    }

    public static String toBinaryString(byte b){
        String s = Integer.toBinaryString(b & 0xFF);
        int len = s.length();
        if(len < 8){
            int offset = 8 - len;
            for(int j=0; j<offset; j++){
                s = "0" + s;
            }
        }
        return s;
    }

    public static boolean getBit(byte b, int position){
        String s = toBinaryString(b);
        char c = s.charAt(7 - position);
        System.out.println(c);
        return c=='1';
    }
	

	public static void main(String[] args) {
        byte b = parseHexString("A0");
	    System.out.println(b);
	    getBit(b, 0);
        getBit(b, 1);
        getBit(b, 2);
        getBit(b, 3);
        getBit(b, 4);
        getBit(b, 5);
        getBit(b, 6);
        getBit(b, 7);

//	    String mode = "03";
//        String remainingWater = new BigInteger(mode, 16).toString(10);
//        System.out.println(remainingWater);
//
//	    String uuid = UUID.randomUUID().toString().replace("-", "");
//	    System.out.println(uuid);
//        System.out.println(UUID.randomUUID().toString());
		
//		Byte one = new Byte("101");
//		int i = 101;
//
//		System.out.println(one == i);
		
//		post("https://api.mch.weixin.qq.com/secapi/pay/refund", "sss");
	}
	
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-type", "text/xml");
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
	}
	
	public static void post(String baseUrl, String param) {
		try {
	        //String urlPath = new String("http://localhost:8080/Test1/HelloWorld?name=丁丁".getBytes("UTF-8"));
//	        String param="name="+URLEncoder.encode("丁丁","UTF-8");
	        //建立连接
	        URL url=new URL(baseUrl);
	        HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();

	        //设置参数
	        httpConn.setDoOutput(true);     //需要输出
	        httpConn.setDoInput(true);      //需要输入
	        httpConn.setUseCaches(false);   //不允许缓存
	        httpConn.setRequestMethod("POST");      //设置POST方式连接
	        //设置请求属性
	        httpConn.setRequestProperty("Content-Type", "text/xml");
	        httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	        httpConn.setRequestProperty("Charset", "UTF-8");
	        //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
	        httpConn.connect();
	        //建立输入流，向指向的URL传入参数
	        DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
	        dos.writeBytes(param);
	        dos.flush();
	        dos.close();
	        //获得响应状态
	        int resultCode=httpConn.getResponseCode();
	        if(HttpURLConnection.HTTP_OK==resultCode){
	            StringBuffer sb=new StringBuffer();
	            String readLine=new String();
	            BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
	            while((readLine=responseReader.readLine())!=null){
	                sb.append(readLine).append("\n");
	            }
	            responseReader.close();
	            System.out.println(sb.toString());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
}
