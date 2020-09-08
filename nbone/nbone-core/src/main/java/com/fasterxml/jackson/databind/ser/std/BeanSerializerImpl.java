package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyMappingWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * <pre class="code">
 *
 *      public static class PageSerializer extends JsonSerializer<Page> {
 *      @Override
 *      public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers)
 *                           throws IOException, JsonProcessingException {
 *          gen.writeStartObject();
 *          gen.writeNumberField("total", value.getTotalElements());
 *          gen.writeNumberField("totalPages", value.getTotalPages());
 *          gen.writeObjectField("list", value.getContent());
 *          gen.writeEndObject();
 *      }
 *  }
 *
 *
 *      SimpleModule simpleModule = new SimpleModule();
 *      simpleModule.addSerializer(Page.class, new PageSerializer());
 *      objectMapper.registerModule(simpleModule);
 *
 * </pre>
 *
 * @author chenyicheng
 */
@SuppressWarnings("unused")
public class BeanSerializerImpl extends BeanSerializerBase {

    private static final long serialVersionUID = 5121780229739981922L;

    //private Map<String, String> serializedMapping;
    /*
    /**********************************************************
    /* Life-cycle: constructors
    /**********************************************************
     */

    protected BeanSerializerImpl(BeanSerializerBase src, BeanPropertyWriter[] properties) {
        super(src, properties, src._filteredProps);
    }


    protected BeanSerializerImpl(BeanSerializerBase src,
                                 ObjectIdWriter objectIdWriter, Object filterId) {
        super(src, objectIdWriter, filterId);
    }

    protected BeanSerializerImpl(BeanSerializerBase src, Set<String> toIgnore) {
        super(src, toIgnore);
    }



    /*
    /**********************************************************
    /* Life-cycle: factory methods, fluent factories
    /**********************************************************
     */


    @Override
    public JsonSerializer<Object> unwrappingSerializer(NameTransformer unwrapper) {
        return new UnwrappingBeanSerializer(this, unwrapper);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
        return new BeanSerializerImpl(this, objectIdWriter, _propertyFilterId);
    }

    @Override
    public BeanSerializerBase withFilterId(Object filterId) {
        return new BeanSerializerImpl(this, _objectIdWriter, filterId);
    }

    @Override
    protected BeanSerializerBase withIgnorals(Set<String> toIgnore) {
        return new BeanSerializerImpl(this, toIgnore);
    }

    /**
     * Implementation has to check whether as-array serialization
     * is possible reliably; if (and only if) so, will construct
     * a {@link BeanAsArraySerializer}, otherwise will return this
     * serializer as is.
     */
    @Override
    protected BeanSerializerBase asArraySerializer() {
        /* Can not:
         *
         * - have Object Id (may be allowed in future)
         * - have "any getter"
         * - have per-property filters
         */
        if ((_objectIdWriter == null)
                && (_anyGetterWriter == null)
                && (_propertyFilterId == null)
        ) {
            return new BeanAsArraySerializer(this);
        }
        // already is one, so:
        return this;
    }

    /*
    /**********************************************************
    /* JsonSerializer implementation that differs between impls
    /**********************************************************
     */

    /**
     * Main serialization method that will delegate actual output to
     * configured
     * {@link BeanPropertyWriter} instances.
     */
    @Override
    public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (_objectIdWriter != null) {
            gen.setCurrentValue(bean); // [databind#631]
            _serializeWithObjectId(bean, gen, provider, true);
            return;
        }
        gen.writeStartObject(bean);
        if (_propertyFilterId != null) {
            serializeFieldsFiltered(bean, gen, provider);
        } else {
            serializeFields(bean, gen, provider);
        }
        gen.writeEndObject();
    }

    /*
    /**********************************************************
    /* Standard methods
    /**********************************************************
     */

    @Override
    public String toString() {
        return "BeanSerializer for " + handledType().getName();
    }

    public static BeanSerializerBase serializedMapping(BeanSerializerBase beanSerializer, Map<String, String> serializedMapping) {
        BeanPropertyWriter[] props = beanSerializer._props;
        BeanPropertyWriter[] usedProps = new BeanPropertyWriter[props.length];
        for (int i = 0; i < props.length; i++) {
            BeanPropertyWriter prop = props[i];
            String name = prop.getName();
            String serializedName = serializedMapping.get(name);
            if (serializedName != null && serializedName.length() > 0) {
                usedProps[i] = new BeanPropertyMappingWriter(prop, serializedName);
            } else {
                usedProps[i] = prop;
            }
        }
        BeanSerializerImpl beanSerializerImpl = new BeanSerializerImpl(beanSerializer, usedProps);
        return beanSerializerImpl;
    }

    public static BeanSerializerBase serializedMapping(ObjectMapper objectMapper, Class<?> type, Map<String, String> serializedMapping) throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        SerializerProvider serializerProvider = objectMapper.getSerializerProviderInstance();
        //SerializerProvider serializerProvider = SerializerProviderUtils.getInstance(objectMapper);
        BeanSerializerBase jsonSerializer = (BeanSerializer) BeanSerializerFactory.instance.createSerializer(serializerProvider, javaType);
        return serializedMapping(jsonSerializer, serializedMapping);
    }


    public static SimpleModule registerModule(ObjectMapper objectMapper, Class<?> type, Map<String, String> serializedMapping) throws JsonMappingException {

        BeanSerializerBase beanSerializerImpl = serializedMapping(objectMapper, type, serializedMapping);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(type, beanSerializerImpl);
        objectMapper.registerModule(simpleModule);
        return simpleModule;
    }


    /**
     * 多个Class 字段映射调整， 且共用一个 mapping  serializedMapping
     *
     * @param objectMapper
     * @param serializedMapping
     * @param types
     * @return
     * @throws JsonMappingException
     */
    public static SimpleModule registerModule(ObjectMapper objectMapper, Map<String, String> serializedMapping, Class<?>... types)
            throws JsonMappingException {

        SimpleModule simpleModule = new SimpleModule();
        for (Class<?> type : types) {
            BeanSerializerBase beanSerializerImpl = serializedMapping(objectMapper, type, serializedMapping);
            simpleModule.addSerializer(type, beanSerializerImpl);
        }
        objectMapper.registerModule(simpleModule);
        return simpleModule;
    }


    /**
     * 自定义 JsonSerializer解析
     *
     * @param objectMapper
     * @param type
     * @param jsonSerializer
     * @param <T>
     * @return
     */
    public static <T> SimpleModule addSerializer(ObjectMapper objectMapper, Class<? extends T> type, JsonSerializer<T> jsonSerializer) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(type, jsonSerializer);
        objectMapper.registerModule(simpleModule);
        return simpleModule;
    }


}
