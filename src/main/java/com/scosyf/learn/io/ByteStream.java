package com.scosyf.learn.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;

/**
 * 学习IO流之前，了解下二进制思维：
 * 所有文件，包括图片，视频，文本，压缩等等都是以0/1的二进制形式保存，而流就是二进制的抽象（网络传输一般就是字节流）
 * 
 * 
 * 如何理解in/out和read/write的关系：
 * 		1.外界（图片，文字，网络）到内部（内核，硬盘）就是in，输入流
 *  	2.进来后，只是一个二进制媒介，为了能存到硬盘中，需要byte[]，也就是要从in读取内容到byte[]中，即read
 *  	3/4.内存中的东西要让外界看到就是out，而为了把内存中的数据弄到外面，就得把byte[]写到out中，即write
 *  
 *		而并非read/write的字面意思：从计算机读取数据就说是read，或是写数据进来就说是write
 * 
 * 
 * Java IO 体系中分为
 * 		流式：字节流 / 字符流
 * 		非流式：File / RandomAccessFile
 * 
 * 		   		 _	
 *		  		/  ByteArrayInputStream
 *			   /   ObjectInputStream	
 * InputStream |-  FileInputStream      _
 * 		  	   \					   /  BufferedInputStream
 * 				\_ FilterInputStream -|	  DataInputStream
 *		   		  	                   \_ PushBackInputStream
 * 		   		  _	
 *		  		 /  ByteArrayOutputStream
 *			    /   ObjectOutputStream	
 * OutputStream |-  FileOutputStream      _
 * 		  	    \					     /  BufferedOututStream
 * 				 \_ FilterOutputStream -|  DataOutputStream
 *		   		  	                     \_ PushBackOutputStream
 * 
 * 
 * 
 * PS：字节流想要追加形式输出，就要使用FileOutputStream，带上append
 *
 */
public class ByteStream {
	
	public static final String BYTE_FILE = CharStream.BASIC_URL + "byteStream.txt";

	public static void main(String[] args) {
//		testFileStream();
//		testByteArrayStream();
//		testDataOutputStream();
		testBufferedStream();
	}
	
	/**
	 * 文件输入/输出流
	 * 
	 * 简单的读写文件，长度固定，且效率低
	 */
	public static void testFileStream() {
		OutputStream os = null;
		try {
			os = new FileOutputStream(BYTE_FILE);
			List<String> datas = Lists.newArrayList("用原始的FileOutputStream", "写入数据");
			for (String data : datas) {
				os.write(data.getBytes(Charset.forName("utf8")));
				os.write("\n".getBytes());
			}
			System.out.println(">>> FileOutputStream写入完成");
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件写入异常 " + e);
		} finally {
			IOUtils.close(os);
		}
		
		
		InputStream is = null;
		try {
			is = new FileInputStream(BYTE_FILE);
			//固定大小的写法，带扩容的参考 CharStream.testInputStreamReader()
			byte[] buf = new byte[1024];
			//FileInputStream的read(byte[])是没有缓冲的，单字节读取，native
		    int bytesRead = is.read(buf);    
		    String data = new String(buf, 0, bytesRead, "UTF-8");
			
		    System.out.println(">>> FileInputStream读取:\n" + data);
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件写入异常 " + e);
		} finally {
			IOUtils.close(is);
		}
	}
	
	/**
	 * 使用字节数组流来读取文件（动态扩展）
	 * 相当于是适配器，作为输出封装了动态数组，便于使用
	 * 
	 * 这里ByteArrayOutputStream是用到接收InputStream读到的数据，仅仅是以字节数组存起来，并无其他用处，也不需要close
	 */
	public static void testByteArrayStream() {
		InputStream is = null;
		try {
			is = new FileInputStream(BYTE_FILE);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte[] buf = new byte[8];
		    int bytesRead = 0;    
		    while((bytesRead = is.read(buf)) != -1) {
		    	baos.write(buf, 0, bytesRead);
		    }
		    byte[] data = baos.toByteArray();
		    System.out.println(">>> ByteArrayOutputStream读取:\n" + new String(data));
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件写入异常 " + e);
		} finally {
			IOUtils.close(is);
		}
	}
	
	/**
	 * 使用缓存流，来包装无缓存的FileXXXStream；虽然ByteArrayXXXStream也是大范围存储，但是如果需要按字节读写，就需要buffered
	 * 
	 * 即【缓存+字节】读写
	 */
	public static void testDataOutputStream() {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(BYTE_FILE)));
			//写入4个字节
			dos.writeInt(2333);
//			dos.writeDouble(2.33);
//			dos.writeUTF("昆布");
			dos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(dos);
		}
		
		
		DataInputStream dis = null;
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(BYTE_FILE));
			//available()方法父类默认实现返回0
			byte[] buf = new byte[is.available()];
			//和FileInputStream的read(byte[])不同，buffered用到了buf
			is.read(buf);
			//int:[0, 0, 9, 29]
			System.out.println(Arrays.toString(buf));
			
			dis = new DataInputStream(is);
			System.out.println(dis.readInt());
			System.out.println(dis.readDouble());
			System.out.println(dis.readUTF());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(dis);
		}
	}
	
	/**
	 * 利用缓存流来提高文件传输（下载文件）
	 */
	public static void testBufferedStream() {
		String data = "qwertyuiopasdfghjklzxcvbnm1234567890";
		byte[] dataByte = data.getBytes();
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new BufferedInputStream(new ByteArrayInputStream(dataByte));
			os = new BufferedOutputStream(new FileOutputStream(BYTE_FILE, true));
			byte[] buf = new byte[16];
			int readSize = 0;
			while((readSize = is.read(buf)) != -1) {
				os.write(buf, 0, readSize);
			}
			os.flush();
			System.out.println(">>> BufferedOutputStream输出完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(os);
			IOUtils.close(is);
		}
	}
	
}
