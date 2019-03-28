package com.scosyf.learn.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 数据传数载体：字节容器ByteBuf
 *
 * 结构：
 *  <---------------------------- maxCapacity ----------------------------------->
 *  <---------------------------- capacity -------------------->
 *  <---- 废弃字节 --> <------- 可读字节 -----> <---- 可写字节 ---> <-- 可扩容字节 --->
 * |_________________|_______________________|__________________|_ _ _ _ _ _ _ _ _|
 *                   ↑                       ↑
 *             读指针 readerIndex       写指针 writerIndex
 *
 * 读取字节：readerIndex自增1
 * 可读范围：writerIndex - readerIndex
 *
 * 写数据：writerIndex自增1
 * 可写范围：capacity - writerIndex（当容量不足时，可进行扩容，最大到maxCapacity，超出报错）
 *
 * @author: KunBu
 * @time: 2019/2/21 13:45
 * @description:
 */
public class ByteBufTest {

    //heapBuffer 可以申请堆内存
    private static final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(9, 100);

    /**
     * 如果capacity不够会扩容
     *
     **/
    public static void capacity() {
        System.out.println("capacity() >>> " + buf.capacity());
        System.out.println("maxCapacity() >>> " + buf.maxCapacity());

        System.out.println("readableBytes() >>> " + buf.readableBytes());
        System.out.println("isReadable() >>> " + buf.isReadable());

        System.out.println("writableBytes() >>> " + buf.writableBytes());
        System.out.println("isWritable() >>> " + buf.isWritable());
        System.out.println("maxWritableBytes() >>> " + buf.maxWritableBytes());
    }

    /**
     * 写指针必须大于读指针
     * 否则报 Exception in thread "main" java.lang.IndexOutOfBoundsException: readerIndex:  (expected: 0 <= readerIndex <= writerIndex())
     *
     **/
    public static void readWritePoint() {
        System.out.println("writerIndex() >>> " + buf.writerIndex());
        //设置写指针位置
        System.out.println("writerIndex(int writerIndex) >>> " + buf.writerIndex(6));

        System.out.println("readerIndex() >>> " + buf.readerIndex());
        //设置读指针位置
        System.out.println("readerIndex(int readerIndex) >>> " + buf.readerIndex(2));

        //mark和reset是一对API，用于业务操作期间调用指针后恢复（等价于buf.readerIndex(buf.readerIndex())）
        System.out.println("markReaderIndex() >>> " + buf.markReaderIndex());

        //业务操作
        System.out.println("readerIndex(int readerIndex) >>> " + buf.readerIndex(3));

        System.out.println("resetReaderIndex() >>> " + buf.resetReaderIndex());
    }

    /**
     * 读写 API：从指针位置开始读写
     *
     **/
    public static void readWrite() {

        System.out.println("writeBytes(byte[] src) >>> " + buf.writeBytes("hello there".getBytes(Charset.forName("utf8"))));
        //dst不能超过readableBytes，一般等于dst不能超过readableBytes
//        byte[] dst = new byte[buf.readableBytes()];
        byte[] dst = new byte[4];
        System.out.println("readBytes(byte[] dst) >>> " + buf.readBytes(dst));
        System.out.println("dst >>> " + Arrays.toString(dst) + ", String: " + new String(dst));

        //set和get不改变指针位置（从测试结果看，只能在读写指针间活动）
//        System.out.println("setBytes(int index, byte[] src) >>> " + buf.setBytes(buf.readerIndex(), "hi".getBytes()));
        System.out.println("setBytes(int index, byte[] src) >>> " + buf.setBytes(buf.writerIndex(), "hi".getBytes()));
        byte[] dst2 = new byte[buf.readableBytes()];
        System.out.println("getBytes(int index, byte[] dst) >>> " + buf.getBytes(buf.readerIndex(), dst2));
        System.out.println("dst2 >>> " + Arrays.toString(dst2) + ", String: " + new String(dst2));

    }

    /**
     * Netty使用堆外内存，需要手动回收
     *
     * 默认创建完ByteBuf，引用计数为1，调用retain加1，调用release减1
     **/
    public static void retainRelease() {
        //如果增加引用计数一定要释放
        System.out.println("retain() >>> " + buf.retain());
        System.out.println("release() >>> " + buf.release());
        System.out.println("release() >>> " + buf.release());
    }

    /**
     * slice()：从原始buf中截取一段，范围读写指针之间，返回新的buf，最大容量为readableBytes
     * duplicate()：把整个buf截取出来。包括所有数据，指针信息
     *
     *  slice和duplicate：内存和引用计数与原始buf共享
     *  ps：因为共享引用计数，所以如果原来的buf调用release，引用计数为0后，slice和duplicate的buf进行读写会报错
     *
     * copy()：从原始buf中拷贝所有信息，并且与原始buf无关（新内存）
     *
     * 这三个方法读写指针都和原始buf无关
     **/
    public static void copy() {
        //考虑到复制后的引用问题，需要加上一次引用，但是这样原始buf就需要释放两次
        System.out.println("old buf >>> " + buf);
        ByteBuf newBuf = buf.retainedSlice();
        System.out.println("new buf >>> " + newBuf);

        System.out.println("old buf release() >>> " + buf.release());
        System.out.println("new buf release() >>> " + buf.release());
    }

    public static void main(String[] args) {
//        capacity();
//        readWritePoint();
//        readWrite();

//        release();
        copy();

        System.out.println(buf.release());
    }
}
