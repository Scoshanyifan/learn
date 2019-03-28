package com.scosyf.learn.net.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO模型中
 * 1.一个线程管理一批连接，轮询它们看看有没有数据要读取
 * 2.读写面向Buffer
 *
 * @author kunbu
 * @time 2019/2/20 11:36
 **/
public class NIOServer {

    public static void main(String[] args) {
        server();
    }

    public static void server() {
        try {
            //线程一：负责轮询是否有新的连接
            Selector serverSelector = Selector.open();
            //线程二：负责轮询连接是否有新的数据可读
            Selector clientSelector = Selector.open();

            new Thread(() -> {
                try {
                    //启动服务
                    ServerSocketChannel listenerChannel = ServerSocketChannel.open();

                    ServerSocket serverSocket = listenerChannel.socket();
                    serverSocket.bind(new InetSocketAddress(8888));

                    listenerChannel.configureBlocking(false);
                    listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                    while (true) {
                        //监听是否有新的连接
                        if (serverSelector.select(1) > 0) {
                            Set<SelectionKey> set = serverSelector.selectedKeys();
                            Iterator<SelectionKey> keys = set.iterator();

                            while (keys.hasNext()) {
                                SelectionKey key = keys.next();

                                if (key.isAcceptable()) {
                                    try {
                                        //每有一个新连接，不再创建新线程，而是注册到选择器上
                                        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                        clientChannel.configureBlocking(false);
                                        clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                    } finally {
                                        keys.remove();
                                    }
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    while (true) {
                        //轮询是否有连接存在
                        if (clientSelector.select(1) > 0) {
                            Set<SelectionKey> set = clientSelector.selectedKeys();
                            Iterator<SelectionKey> keys = set.iterator();

                            while (keys.hasNext()) {
                                SelectionKey key = keys.next();
                                //连接是否可读
                                if (key.isReadable()) {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ByteBuffer buf = ByteBuffer.allocate(1024);

                                    clientChannel.read(buf);
                                    buf.flip();

                                    String result = Charset.defaultCharset().newDecoder().decode(buf).toString();
                                    System.out.println(result);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
