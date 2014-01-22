package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("rdnConv")
@List({ @Converts(from = String.class, to = Rdn.class), @Converts(from = Rdn.class, to = String[].class) })
@Scope("singleton")
public class RdnConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_RDN = TypeDescriptor.valueOf(Rdn.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_RDN)) {
            Rdn sourceRdn = (Rdn) source;

            return new String[] { sourceRdn.getName(), sourceRdn.getValue().getString() };
        } else {
            return new Rdn((String) source);
        }
    }
}
