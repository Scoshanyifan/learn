package com.scosyf.learn.IM.utils;

import com.scosyf.learn.IM.common.Packet;
import com.scosyf.learn.IM.common.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 通讯协议：
 *
 * |魔数|版本号|序列化算法|指令|数据长度|数据内容|
 *   4    1        1      1     4       n
 *
 * @author: KunBu
 * @time: 2019/2/21 18:14
 * @description:
 */
public class PacketUtils {

    private static final int MAGIC_NUMBER = 0x2333;

    public ByteBuf encode(Packet packet) {
        //创建IO读写的直接内存，不受JVM管理
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();

        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        Packet packet = null;

        return packet;
    }
}
