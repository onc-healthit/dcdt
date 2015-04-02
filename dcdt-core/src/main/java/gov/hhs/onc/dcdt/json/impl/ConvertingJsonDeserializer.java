package gov.hhs.onc.dcdt.json.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

@SuppressWarnings({ "serial" })
public class ConvertingJsonDeserializer<T, U> extends StdDeserializer<U> {
    @Autowired
    protected ConversionService convService;

    protected Class<T> srcClass;
    protected Class<U> targetClass;

    protected ConvertingJsonDeserializer(Class<T> srcClass, Class<U> targetClass) {
        super(targetClass);

        this.srcClass = srcClass;
        this.targetClass = targetClass;
    }

    @Nullable
    @Override
    public U deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return this.convService.convert(jsonParser.readValueAs(this.srcClass), this.targetClass);
    }
}
