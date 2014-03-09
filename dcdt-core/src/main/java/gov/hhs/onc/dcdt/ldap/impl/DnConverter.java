package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolDnUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("dnConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = Dn.class) }, serialize = { @Converts(from = Dn.class, to = String.class) })
@List({ @Converts(from = String.class, to = Dn.class), @Converts(from = String[].class, to = Dn.class), @Converts(from = Rdn[].class, to = Dn.class),
    @Converts(from = Dn.class, to = Rdn[].class), @Converts(from = Dn.class, to = String.class) })
public class DnConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_DN = TypeDescriptor.valueOf(Dn.class);
    private final static TypeDescriptor TYPE_DESC_RDN_ARR = TypeDescriptor.array(TypeDescriptor.valueOf(Rdn.class));

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_DN)) {
            return ToolCollectionUtils.toArray(((Dn) source).getRdns(), Rdn.class);
        } else if (sourceType.isAssignableTo(TYPE_DESC_RDN_ARR)) {
            return ToolDnUtils.fromRdns(((Rdn[]) source));
        } else if (sourceType.isAssignableTo(TYPE_DESC_RDN_ARR)) {
            return ToolDnUtils.fromStrings(((String[]) source));
        } else {
            return ToolDnUtils.fromStrings(((String) source));
        }
    }
}
