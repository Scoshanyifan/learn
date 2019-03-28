package com.scosyf.learn.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * 启动Netty服务器
 *  1.指定县城模型
 *  2.指定IO模型
 *  3.连接的读写逻辑
 *
 * @author: KunBu
 * @time: 2019/2/20 14:11
 * @description:
 */
public class NettyServer {

    public static void main(String[] args) {
        server();
    }

    public static void server() {

        //负责接收新连接
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //负责读取数据
        NioEventLoopGroup worker = new NioEventLoopGroup();
        //引导类，负责启动初始化
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                //1.配置两大线程组
                .group(boss, worker)
                //2.指定服务端模型为NIO
                .channel(NioServerSocketChannel.class)
                //3.定义每条连接数据读写，业务逻辑等等（这里NioSeverSocketChannel和NioSocketChannel是对NIO模型的连接的抽象）
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) {
//                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//                                System.out.println(msg);
//                            }
//                        });

                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });

//        serverBootstrap.bind(8888);
        bindPort(serverBootstrap, 8888);


    }

    /**
     * 递增式自动绑定端口
     *
     * 原理：bind()方法是异步的，返回ChannelFuture，给它添加监听器，监听是否绑定成功
     *
     * @author kunbu
     * @time 2019/2/20 15:11
     **/
    private static void bindPort(ServerBootstrap serverBootstrap, int port) {

        int i;
        serverBootstrap
                .bind(port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {

                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("端口[" + port + "]绑定成功");
                        } else {
                            System.out.println("端口[" + port + "]绑定失败，继续尝试");
                            int portNext = port + 1;
                            bindPort(serverBootstrap, portNext);
                        }
                    }
                });
    }

}

/**
 * 自定义数据处理逻辑
 *
 * @author kunbu
 * @time 2019/2/20 16:59
 **/
class FirstServerHandler extends ChannelInboundHandlerAdapter {

    //收到客户端发来的数据时回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(">>> " + new Date() + " 服务端读到数据：" + buf.toString(Charset.defaultCharset()));

        System.out.println(">>> " + new Date() + " 服务端回应");
        ctx.channel().writeAndFlush(getByteBuf(ctx));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "服务端收到消息了，ok".getBytes(Charset.forName("utf8"));

        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(bytes);

        return buf;
    }
}