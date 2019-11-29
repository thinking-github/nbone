package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019-11-24
 */
public class BeanPropertyMappingWriter extends BeanPropertyWriter {

    private BeanPropertyWriter base;
    /**
     * 序列化新名称
     */
    private SerializedString name;

    public BeanPropertyMappingWriter(BeanPropertyWriter base, String name) {
        this(base, new SerializedString(name));
    }

    public BeanPropertyMappingWriter(BeanPropertyWriter base, SerializedString name) {
        super(base);
        this.base = base;
        this.name = name;
    }

    /**
     * @see BeanPropertyWriter#serializeAsField(Object, JsonGenerator, SerializerProvider)
     */
    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        // inlined 'get()'
        final Object value = (_accessorMethod == null) ? _field.get(bean)
                : _accessorMethod.invoke(bean);

        // Null handling is bit different, check that first
        if (value == null) {
            if (_nullSerializer != null) {
                gen.writeFieldName(getSerializedName());
                _nullSerializer.serialize(null, gen, prov);
            }
            return;
        }
        // then find serializer to use
        JsonSerializer<Object> ser = _serializer;
        if (ser == null) {
            Class<?> cls = value.getClass();
            PropertySerializerMap m = _dynamicSerializers;
            ser = m.serializerFor(cls);
            if (ser == null) {
                ser = _findAndAddDynamic(m, cls, prov);
            }
        }
        // and then see if we must suppress certain values (default, empty)
        if (_suppressableValue != null) {
            if (MARKER_FOR_EMPTY == _suppressableValue) {
                if (ser.isEmpty(prov, value)) {
                    return;
                }
            } else if (_suppressableValue.equals(value)) {
                return;
            }
        }
        // For non-nulls: simple check for direct cycles
        if (value == bean) {
            // three choices: exception; handled by call; or pass-through
            if (_handleSelfReference(bean, gen, prov, ser)) {
                return;
            }
        }
        gen.writeFieldName(getSerializedName());
        if (_typeSerializer == null) {
            ser.serialize(value, gen, prov);
        } else {
            ser.serializeWithType(value, gen, prov, _typeSerializer);
        }
    }


    @Override
    public String getName() {
        if (name != null) {
            return name.getValue();
        }
        return super.getName();
    }

    @Override
    public SerializableString getSerializedName() {
        if (name != null) {
            return name;
        }
        return super.getSerializedName();
    }
}
