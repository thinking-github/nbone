package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019-11-24
 */
public class SerializerProviderUtils {


    public static SerializerProvider getInstance(SerializerProvider src, SerializationConfig config,
                                                 SerializerFactory f) {

        SerializerProvider serializerProvider = new DefaultSerializerProvider.Impl(src, config, f);

        return serializerProvider;
    }

    public static SerializerProvider getInstance(ObjectMapper objectMapper) {

        SerializerProvider serializerProvider = new DefaultSerializerProvider.Impl(
                objectMapper.getSerializerProvider(),
                objectMapper.getSerializationConfig(),
                objectMapper.getSerializerFactory());

        return serializerProvider;
    }

  /*  public static SerializerProvider getInstance(ObjectMapper objectMapper) {

        return objectMapper.getSerializerProviderInstance();
    }*/
}
