package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("ldapAttrConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = Attribute.class) }, serialize = { @Converts(from = Attribute.class, to = String.class) })
@List({ @Converts(from = String.class, to = Attribute.class), @Converts(from = Attribute.class, to = String.class) })
public class LdapAttributeConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_LDAP_ATTR = TypeDescriptor.valueOf(Attribute.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return (sourceType.isAssignableTo(TYPE_DESC_LDAP_ATTR) ? ToolLdapAttributeUtils.writeAttribute(((Attribute) source)) : ToolLdapAttributeUtils
            .readAttribute(((String) source)));
    }
}
