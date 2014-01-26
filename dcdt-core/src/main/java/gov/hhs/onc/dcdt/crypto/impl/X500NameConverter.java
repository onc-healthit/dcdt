package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import java.util.Objects;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("x500NameConv")
@ConvertsJson(deserialize = @Converts(from = String.class, to = X500Name.class), serialize = @Converts(from = X500Name.class, to = String.class))
@ConvertsUserType(X500NameUserType.class)
@List({ @Converts(from = String.class, to = X500Name.class), @Converts(from = X500Name.class, to = String.class) })
@Scope("singleton")
public class X500NameConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_X500_NAME = TypeDescriptor.valueOf(X500Name.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return sourceType.isAssignableTo(TYPE_DESC_X500_NAME) ? Objects.toString(source) : new X500Name((String) source);
    }
}
