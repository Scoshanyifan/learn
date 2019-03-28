package com.scosyf.learn.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;

/**
 * 【字节流/字符流】区别：
 * 字节流没有缓冲区，是直接输出的，而字符流是输出到缓冲区的。因此在输出时，字节流不调用close()方法时，信息已经输出了，
 * 而字符流只有在调用close()方法关闭缓冲区时，信息才输出。要想字符流在未关闭时输出信息，则需要手动调用flush()方法。
 *
 * [程序] ---> [字节流] ----> [文件]
 * [程序] ---> [字符流] ----> [缓存] ----> [文件]
 *
 *
 *
 * 字符流：建立在字节流的基础上
 * 		   _	
 *		  /  InputStreamReader ---子类---> FileReader
 * Reader |- BufferedReader
 * 		  \_ CharArrayReader / StringReader（本质和charArr一样。因为底层还是char[]）	
 *		   _	 
 *        /  OutputStreamWriter ---子类---> FileWriter
 * Writer |- BufferedWriter
 * 		  \_ CharArrayWriter / StringWriter	
 * 
 * 
 * PS：字符流指定字符集需要使用OutputStreamWriter或InputStreamReader
 */
public class CharStream {
	
	public static final String BASIC_URL = "D:\\scosyf\\learn\\";
	public static final String CHAR_FILE = BASIC_URL + "charStream.txt";

	public static void main(String[] args) {
		//写入测试
		testOutputStreamWriter();
//		testBufferedWriter();
//		testPrintWriter();

		//读取测试
		testInputStreamReader();
//		testBufferedReader();
	}
	
	/**
	 * 基础的字符流获取文本文件，建立在字节流之上
	 * 
	 * 优化：带自动扩容功能（模仿ByteArrayOutputStream）
	 */
	public static void testInputStreamReader() {
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(CHAR_FILE), Charset.forName("utf8"));
			//初始大小
			int size = 8;
			//容器
			char[] data = new char[size];
			//缓存
			char[] cbuf = new char[size];
			//实际数据量
			int count = 0;
			//缓存读取量
			int charRead = 0;
			while((charRead = isr.read(cbuf)) != -1) {
				//每次读完还有剩下就扩容
				if (charRead == size) {
					data = Arrays.copyOf(data, size * 2 + count);
				}
				//复制（把新的缓存cbuf，从[0, charRead]添加到目标的相应位置，即从count开始，count前是老数据）
				System.arraycopy(cbuf, 0, data, count, charRead);
				count += charRead;
			}
			String str = new String(data, 0, count);
			System.out.println(">>> InputStreamReader读取: \n" + str);
			
			//这是没有扩容，固定大小
//			char[] cbuf2 = new char[1024];
//			int charRead2 = isr.read(cbuf2);
//			String str2 = new String(cbuf2, 0, charRead2);
//			System.out.println(">>> InputStreamReader2: \n" + str2);
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件读取异常 " + e);
		} finally {
			IOUtils.close(isr);
		}
	}
	
	/**
	 * 基础字符输出流
	 */
	public static void testOutputStreamWriter() {
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(CHAR_FILE));
			List<String> lines = Lists.newArrayList("用OutputStreamWriter", "的write()", "写入数据");
			for (String line : lines) {
				osw.write(line);
				osw.write("\n");
			}
			//如果没有close，也没有flush，数据不会输出，而是一直再缓冲区
//			osw.flush();
			System.out.println(">>> OutputStreamWriter写入完成");
		} catch(FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件写入异常 " + e);
		}
//		finally {
//			IOUtils.close(osw);
//		}
	}
	
	/**
	 * 通过缓存字符流获取文本文件
	 * 
	 * FileReader仅仅是继承了InputstreamReader而已，调用了父类的构造函数：super(new FileInputStream(fileName));
	 */
	public static void testBufferedReader() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(CHAR_FILE)); //和下面不同的是，下面套了一层OutputStreamWriter用来指定字符集
			StringBuilder lines = new StringBuilder();
			
			String line = null;
			while((line = br.readLine()) != null) {
				lines.append(line).append("\n");
			}
			System.out.println(">>> BufferedReader读取:\n" + lines);
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件读取异常 " + e);
		} finally {
			IOUtils.close(br);
		}
	}
	
	/**
	 * 用缓冲输出流写到文件
	 * 
	 * 1.和上面用FileReader不同，不能指定字符集，如需指定用到OutputStreamWriter
	 * 2.通过FileOutputStream可以指定是否追加，pw不能追加
	 */
	public static void testBufferedWriter() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CHAR_FILE, true), Charset.defaultCharset()));
			
			List<String> lines = Lists.newArrayList("用BufferedWriter", "追加形式", "写入数据");
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
			System.out.println(">>> BufferedWriter写入完成");
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} catch (IOException e) {
			System.err.println(">>> 文件写入异常 " + e);
		} finally {
			IOUtils.close(bw);
		}
	}
	
	/**
	 * 输出包装类PrintWriter
	 * 
	 * 会覆盖原内容，即不能追加
	 */
	public static void testPrintWriter() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(CHAR_FILE);
			
			List<String> lines = Lists.newArrayList("用PrintWriter", "的println方法", "写入数据（会覆盖）");
			for (String line : lines) {
				pw.println(line);
			}
			System.out.println(">>> PrintWriter写入完成");
		} catch (FileNotFoundException e) {
			System.err.println(">>> 文件不存在 " + e);
		} finally {
			IOUtils.close(pw);
		}
	}
	
}
