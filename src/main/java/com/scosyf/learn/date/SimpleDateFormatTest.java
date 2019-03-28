package com.scosyf.learn.date;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;





import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 测试sdf的非线程安全问题，以及如何解决
 * 
 * @author syf
 * 2018年12月11日
 */
public class SimpleDateFormatTest {
	
	private static final int LOOP_TIME					 = 100;

	private static SimpleDateFormat sdf 				 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static ThreadFactory factory 				 = new ThreadFactoryBuilder()
															.setNameFormat("sdf-pool-%d")
															.build();
	private static ExecutorService pool					 = new ThreadPoolExecutor(10, 100, 0L, 
															TimeUnit.MILLISECONDS, 
															new ArrayBlockingQueue<Runnable>(1024),
															factory,
															new ThreadPoolExecutor.AbortPolicy());
	//控制器
	private static CountDownLatch count					 = new CountDownLatch(LOOP_TIME);
	
	//ThreadLocal 可以确保每个线程都可以得到单独的一个 SimpleDateFormat 的对象
	private static ThreadLocal<SimpleDateFormat> sdfPool = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/**
	 * 如果使用静态sdf，结果会出现时间跳跃，或是不准的情况
	 * 
	 * 原因：
	 * 源码总有以下一段代码，线程-2会把线程-1的date设置成自己的，导致出错
	 * // Convert input date to time field list
     *    calendar.setTime(date);
     *    
  	 * 解决：
	 * 1.使用局部变量
	 * 2.加锁（控制粒度）
	 * 3.使用ThreadLocal
	 * 4.JDK >= 1.8 使用DateTimeFormatter
	 */
	public static void testConcurrency() throws InterruptedException {
		//用set是会出现问题的
		//Set<String> dates = Sets.newConcurrentHashSet();
		Map<Integer, String> dates = Maps.newConcurrentMap();
		for (int i = 0; i < LOOP_TIME; i++) {
			long time = System.currentTimeMillis() + i * 24 * 3600 * 1000L;
			final int fi = i;
			pool.execute(() -> {
				String timeStr = null;
				//1.局部变量
//				sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				//3.ThreadLocal
//				sdf = sdfPool.get();
				
				//2.同步锁
//				synchronized (sdf) {
					//4.DateTimeFormatter
//					timeStr = LocalDateTime
//							.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
//							.format(formatter);
					timeStr = sdf.format(new Date(time));
					dates.put(fi, timeStr);
//				}
				count.countDown();
			});
		}
		//保证任务执行完再走主函数
		count.await();
		System.out.println("test over, dates size: " + dates.size());
		for (String date : dates.values()) {
			System.out.println(date);
		}
		pool.shutdown();
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		testConcurrency();
	}
}
