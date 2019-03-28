package com.scosyf.learn.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * ThreadLocal 为每一个使用该变量的线程提供一个副本，随线程走，不产生冲突
 * PS: 不是用来解决共享对象的多线程访问问题的
 *
 * 使用场景：类似于DateFormat，connection这种不需要每次使用都new的变量，可以给每个线程独立访问
 *
 * https://segmentfault.com/a/1190000010251063
 * https://www.cnblogs.com/dolphin0520/p/3920407.html
 *
 * @author: KunBu
 * @time: 2019/2/18 16:41
 */
public class ThreadLocalUse {

    private static final int THREAD_COUNT        = 100;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Date NOW_TIME           = new Date();

    public static void main(String[] args) {

        testSimpleDateFormat();
//        testThreadLocal();
    }

    /**
     * 测试并发环境下SimpleDateFormat的问题
     *
     * SimpleDateFormat 在并发环境下回出现问题，原因是其中使用的同一个calendar
     * 1.8下没有出现问题，待测试
     *
     * @author kunbu
     * @time 2019/2/18 17:49
     **/
    public static void testSimpleDateFormat() {

        ExecutorService pool = ThreadPool.getThreadPool();
        for(int i = 0; i < THREAD_COUNT; i++) {
            pool.execute(() -> {
                    String dateStr = FORMAT.format(NOW_TIME);
                    System.out.println(Thread.currentThread().getName() + " >>> " + dateStr);
                }
            );
        }
        pool.shutdown();
    }

    public static void testThreadLocal() {

        for(int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                String dateStr = DateFormatHelper.format(NOW_TIME);
                System.out.println(Thread.currentThread().getName() + " >>> " + dateStr);
            }).start();
        }
    }


    /**
     * 解决SimpleDateFormat并发问题办法：
     *      1.方法层面加上synchronized（加大线程间竞争和血环效率）
     *      2.每次使用都新建SimpleDateFormat对象（加大GC负担）
     *      3.推荐：使用ThreadLocal（创建的SimpleDateFormat对象不会超过使用的线程数，即同一线程使用的sdf是同一个）
     *
     * @author kunbu
     * @time 2019/2/18 17:31
     **/
    static class DateFormatHelper {

        public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

        private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(
                () -> {return new SimpleDateFormat(DATE_PATTERN_DEFAULT);}
        );

        public static String format(Date date) {
            return SDF.get().format(date);
        }

        public static Date parse(String source) throws ParseException {
            return SDF.get().parse(source);
        }
    }
}
