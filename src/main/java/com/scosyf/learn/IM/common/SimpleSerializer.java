package com.scosyf.learn.IM.common;

import com.alibaba.fastjson.JSON;

/**
 * @author: KunBu
 * @time: 2019/2/21 17:25
 * @description:
 */
public class SimpleSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return Constant.JSON;
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
