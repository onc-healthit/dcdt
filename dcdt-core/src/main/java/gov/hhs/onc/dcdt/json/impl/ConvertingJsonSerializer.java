package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class ConvertingJsonSerializer<T, U> extends StdSerializer<T> {
    protected final static long serialVersionUID = 0L;

    @Autowired
    protected ConversionService convService;

    protected Class<T> srcClass;
    protected Class<U> targetClass;

    protected ConvertingJsonSerializer(Class<T> srcClass, Class<U> targetClass) {
        super(srcClass);

        this.srcClass = srcClass;
        this.targetClass = targetClass;
    }

    @Override
    public void serialize(T src, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        serializerProvider.defaultSerializeValue(this.convService.convert(src, this.targetClass), jsonGen);
    }
}
