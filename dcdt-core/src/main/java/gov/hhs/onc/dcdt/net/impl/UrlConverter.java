package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import java.net.URL;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("urlConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = URL.class) }, serialize = { @Converts(from = URL.class, to = String.class) })
@ConvertsUserType(UrlUserType.class)
@List({ @Converts(from = String.class, to = URL.class), @Converts(from = URL.class, to = String.class) })
@Scope("singleton")
public class UrlConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_URL = TypeDescriptor.valueOf(URL.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return sourceType.isAssignableTo(TYPE_DESC_URL) ? Objects.toString(source) : new URL((String) source);
    }
}
