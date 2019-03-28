package com.scosyf.learn.IM.common;

import lombok.Data;

/**
 * @author: KunBu
 * @time: 2019/2/21 17:20
 * @description:
 */
@Data
public class LoginRequestPacket extends Packet {

    private Long userId;
    private String userName;
    private String password;

    @Override
    public Byte getCommand() {
        return Constant.LOGIN_REQUEST;
    }
}
