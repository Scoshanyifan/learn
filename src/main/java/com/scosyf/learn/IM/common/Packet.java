package com.scosyf.learn.IM.common;

import lombok.Data;

/**
 * @author: KunBu
 * @time: 2019/2/21 17:06
 * @description:
 */
@Data
public abstract class Packet {
    /** 协议版本 */
    private Byte version = 1;

    /** 指令 */
    public abstract Byte getCommand();
}
