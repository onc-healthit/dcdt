package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import java.net.URI;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("uriConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = URI.class) }, serialize = { @Converts(from = URI.class, to = String.class) })
@ConvertsUserType(UriUserType.class)
@List({ @Converts(from = String.class, to = URI.class), @Converts(from = URI.class, to = String.class) })
public class UriConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_URI = TypeDescriptor.valueOf(URI.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return sourceType.isAssignableTo(TYPE_DESC_URI) ? Objects.toString(source) : new URI((String) source);
    }
}
