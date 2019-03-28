package com.scosyf.learn.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: KunBu
 * @time: 2019/2/20 14:20
 * @description:
 */
public class NettyClient {

    /** 最大重试次数 */
    public static final int MAX_RETRY = 5;

    public static void main(String[] args)  {
        client();
    }

    public static void client() {

        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap
                //1.指定线程模型
                .group(group)
                //2.指定IO模型为NIO
                .channel(NioSocketChannel.class)
                //3.处理逻辑
                .handler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) {
                        //责任链模式，逻辑处理链
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                })
                //超时时间，超过后表示连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启TCP底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //是否提高实时性
                .option(ChannelOption.TCP_NODELAY, true);

        //4.建立连接
        connectWithRetry(bootstrap, "localhost", 8888, MAX_RETRY);
    }

    /**
     * 重连机制（指数形式）
     *
     * @author kunbu
     * @time 2019/2/20 16:14
     **/
    private static void connectWithRetry(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap
                .connect(host, port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {

                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("连接成功");
                        } else if (retry <= 0) {
                            System.out.println("重连次数已用完");
                        } else {
                            int retryTime = MAX_RETRY - retry + 1;
                            //1s, 4s, 16s...
                            int delay = 1 << (retryTime * 2);
                            System.err.println(new Date() + "：连接失败，第" + retryTime + "次重连...");
                            //返回的group就是开头配置的线程模型
                            EventLoopGroup group = bootstrap.config().group();
                            group.schedule(() -> {
                                connectWithRetry(bootstrap, host, port, retry - 1);
                            }, delay, TimeUnit.SECONDS);

                        }
                    }
                });
    }

}

/**
 * 业务处理逻辑
 *
 * @author kunbu
 * @time 2019/2/20 16:59
 **/
class FirstClientHandler extends ChannelInboundHandlerAdapter {

    //客户端连接建立后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>> " + new Date() + " 客户端写数据");

        //1.获取数据
        ByteBuf buf = getByteBuf(ctx, "hello");
        //2.写数据
        ctx.channel().writeAndFlush(buf);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(">>> " + new Date() + " 客户端读到数据：" + buf.toString(Charset.defaultCharset()));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg) {
        //内存管理器
        ByteBufAllocator allocator = ctx.alloc();
        //获取二进制抽象
        ByteBuf buf = allocator.buffer();
        //指定数据字符集
        byte[] bytes = msg.getBytes(Charset.forName("utf8"));
        //填充二进制数据到buf
        buf.writeBytes(bytes);

        return buf;
    }
}
