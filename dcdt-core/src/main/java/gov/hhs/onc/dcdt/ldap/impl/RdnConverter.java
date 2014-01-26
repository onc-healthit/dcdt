package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.JsonConverts;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("rdnConv")
@JsonConverts(deserialize = @Converts(from = String.class, to = Rdn.class), serialize = @Converts(from = Rdn.class, to = String.class))
@List({ @Converts(from = String.class, to = Rdn.class), @Converts(from = Rdn.class, to = String[].class), @Converts(from = Rdn.class, to = String.class) })
@Scope("singleton")
public class RdnConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_RDN = TypeDescriptor.valueOf(Rdn.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_RDN)) {
            Rdn sourceRdn = (Rdn) source;

            return targetType.isAssignableTo(TYPE_DESC_STR_ARR) ? ArrayUtils.toArray(sourceRdn.getName(), sourceRdn.getValue().getString()) : ToolLdapUtils
                .toString(sourceRdn);
        } else {
            return new Rdn((String) source);
        }
    }
}
