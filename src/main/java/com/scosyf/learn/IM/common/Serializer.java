package com.scosyf.learn.IM.common;

/**
 * @author: KunBu
 * @time: 2019/2/21 17:22
 * @description:
 */
public interface Serializer {

    Serializer DEFAULT = new SimpleSerializer();

    byte getSerializerAlgorithm();

    byte[] serialize(Object obj);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
