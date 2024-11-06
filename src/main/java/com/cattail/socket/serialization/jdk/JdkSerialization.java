package com.cattail.socket.serialization.jdk;

import com.cattail.socket.serialization.RpcSerialization;

import java.io.*;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/11/6
 * @Copyright: https://github.com/CatTailzz
 */
public class JdkSerialization implements RpcSerialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        byte[] data = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(obj);
            output.flush();
            output.close();
            data = os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            ObjectInputStream input = new ObjectInputStream(is);
            Object result = input.readObject();
            return ((T) result);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}