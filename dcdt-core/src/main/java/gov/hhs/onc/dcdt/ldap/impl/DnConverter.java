package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolDnUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convDn")
@Converts({ @Convert(from = String[].class, to = Dn.class), @Convert(from = Rdn[].class, to = Dn.class), @Convert(from = Dn.class, to = Rdn[].class) })
public class DnConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_DN = TypeDescriptor.valueOf(Dn.class);
    private final static TypeDescriptor TYPE_DESC_RDN_ARR = TypeDescriptor.array(TypeDescriptor.valueOf(Rdn.class));

    @Nullable
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        if (srcType.isAssignableTo(TYPE_DESC_RDN_ARR)) {
            return ToolDnUtils.fromRdns(((Rdn[]) src));
        } else if (srcType.isAssignableTo(TYPE_DESC_STR_ARR)) {
            return ToolDnUtils.fromStrings(((String[]) src));
        } else {
            return ToolCollectionUtils.toArray(((Dn) src).getRdns(), Rdn.class);
        }
    }
}
