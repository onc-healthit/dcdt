package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("x500NameConv")
@List({ @Converts(from = String.class, to = X500Name.class) })
@Scope("singleton")
public class X500NameConverter extends AbstractToolConverter {
    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) {
        return new X500Name((String) source);
    }
}
