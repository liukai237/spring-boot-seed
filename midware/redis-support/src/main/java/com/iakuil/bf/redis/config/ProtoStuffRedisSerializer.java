package com.iakuil.bf.redis.config;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ProtoStuffRedisSerializer implements RedisSerializer<Object> {

    private static final Schema<ObjectWrapper> schema = RuntimeSchema.getSchema(ObjectWrapper.class);

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(new ObjectWrapper(t), schema, buffer);
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            ObjectWrapper objectWrapper = new ObjectWrapper();
            ProtostuffIOUtil.mergeFrom(bytes, objectWrapper, schema);
            return objectWrapper.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class ObjectWrapper {
        private Object object;

        ObjectWrapper() {
        }

        ObjectWrapper(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
