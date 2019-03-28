package com.scosyf.learn.thread;

/**
 * 线程中断有两种场景：
 * 		1.中断【条件的等待】的线程，需要能够响应线程中断，并抛出InterruptedException，包括sleep，wait，join
 * 			PS：线程等待分两种，锁等待（锁外）和条件等待（锁内），这里指的是条件等待
 * 		2.中断【正在运行】的线程，依赖中断标记，如业务代码中的for循环，可以通过thread.isInterrupted()来判断，是否有中断的通知，如果有就处理，提前跳出循环
 * 
 * 线程中断相关的方法：
 * 		1.thread.interrupt()：尝试中断线程，其中会设置中断标记
 * 		2.thread.isInterrupted()：返回中断标记，不清除
 * 		3.Thread.interrupted()：类方法，返回中断标记，并清除标记（置为false）
 * 
 * PS：2.3都调同一方法，区别在于是否清除中断标记 private native boolean isInterrupted(boolean ClearInterrupted);
 * 
 * https://www.cnblogs.com/skywang12345/p/3479949.html
 * 
 * @author scosyf
 */
public class ThreadInterrupt {

    public static void main(String[] args) throws InterruptedException {
    	System.out.println("-------------- 中断【条件等待】的线程 ---------------------------------");
        interruptWait();
        System.out.println("-------------- 中断【正在运行】的线程 ---------------------------------");
        interruptRun();
    }
    
    /**
     * 1.演示如何中断"阻塞状态"下的线程
     *      线程自己响应中断的异常，清除标记并抛出异常来中断
     *      
     * 当目标线程调用了sleep(), wait(), join()等方法而进入阻塞状态，若此时外界调用线程的interrupt()会将线程的中断标记设为true。
     * 由于处于阻塞状态，目标线程会响应中断，同时产生一个InterruptedException异常，并清除标记。
     * 
     * @throws InterruptedException
     */
    public static void interruptWait() throws InterruptedException {
        Thread thread = new WaitingThread();
        printThread(thread);
        thread.start();
        printThread(thread);
        //让出CPU给子线程执行while，循环2次后的第三次进入sleep，此时的状态是TIME_WAITING
        Thread.sleep(2500); 

        thread.interrupt(); 
        //这里的中断标记打印，如果先于子线程捕获抛异常会显示true，即还没清除前（一般都打印false）
        printThread(thread, "main发起中断后 ");
        
        //让出CPU给子线程去执行中断
        Thread.sleep(1000);
        //thread.join();
        printThread(thread);
    }
    
    static class WaitingThread extends Thread {
        @Override
        public void run() {
            try {
                int i = 0;
                printThread(Thread.currentThread(), "进入while前 ");
                while (!isInterrupted()) {
                    Thread.sleep(1000); //第三次sleep的时候中断，在这里抛出异常
                    printThread(Thread.currentThread(), "loop-" + i + " ");
                    i++;
                }
                printThread(Thread.currentThread(), "循环while结束 ");	//这句话不会被打印
            } catch (InterruptedException e) {
                //阻塞状态下的中断会抛出异常，并且会将interrupt标记清除，重置为false
            	printThread(Thread.currentThread(), "抛出异常后捕获 ");
            }
            printThread(Thread.currentThread(), "run()结束 ");
        }
    }
    

    /**
     * 2.演示如何中断"运行状态"下的线程（两者异曲同工，都是以通知方式告诉目标线程）
     *      [1] interrupt()将标记设为true
     *      [2] 通过额外标记来控制
     * @throws InterruptedException
     */
    public static void interruptRun() throws InterruptedException {
    	Thread thread = new RunningThread();
    	printThread(thread);
        thread.start();
        printThread(thread);

        Thread.sleep(1);
        //主线程调用目标线程的中断方法，通知对方，我想让你中断，此时中断标记被置为true，而具体处理由目标线程自己处理
        thread.interrupt();  	// [1]
        //thread.stopThread();  // [2]
        printThread(thread, "main发起中断后 ");

        //Thread.sleep(1000);
        thread.join();
        printThread(thread);	//主动方式去中断，标记在线程结束时才清除
    }
    
    static class RunningThread extends Thread {
        private volatile boolean flag = true;

        public void stopThread() {
            flag = false;
        }

        @Override
        public void run() {
            int i = 0;
            printThread(Thread.currentThread(), "进入while前 ");
            while (!isInterrupted()) {  //[1]
//        	while (flag) {           	//[2]
                i++;
                printThread(Thread.currentThread(), "loop-" + i + " ");
            }
            printThread(Thread.currentThread(), "被中断，循环while结束 ");
        }
    }

    
    private static void printThread(Thread thread) {
    	printThread(thread, "");
    }
    
    private static void printThread(Thread thread, String prefix) {
    	System.out.println(prefix + thread.getName() + ": " + thread.getState() + ", interrupt: " + thread.isInterrupted());
    }
}
