package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("ldapAttrConv")
@ConvertsJson(deserialize = @Converts(from = String.class, to = Attribute.class), serialize = @Converts(from = Attribute.class, to = String.class))
@List({ @Converts(from = String[].class, to = Attribute.class), @Converts(from = String.class, to = Attribute.class),
    @Converts(from = Attribute.class, to = String[].class), @Converts(from = Attribute.class, to = String.class) })
@Scope("singleton")
public class LdapAttributeConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_LDAP_ATTR = TypeDescriptor.valueOf(Attribute.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_LDAP_ATTR)) {
            Attribute sourceLdapAttr = (Attribute) source;

            return targetType.isAssignableTo(TYPE_DESC_STR_ARR) ? ArrayUtils.add(
                ToolLdapAttributeUtils.getValueStrings(sourceLdapAttr).toArray(new String[sourceLdapAttr.size()]), 0, sourceLdapAttr.getUpId()) : Objects
                .toString(sourceLdapAttr);
        } else if (sourceType.isAssignableTo(TYPE_DESC_STR_ARR)) {
            String[] sourceStrs = (String[]) source;

            return (ArrayUtils.getLength(sourceStrs) > 1) ? new DefaultAttribute(sourceStrs[0], ToolArrayUtils.slice(sourceStrs, 1)) : new DefaultAttribute(
                sourceStrs[0]);
        } else {
            MutablePair<String, String[]> ldapAttrStrParts = ToolLdapAttributeUtils.getStringParts((String) source);

            return new DefaultAttribute(ldapAttrStrParts.getLeft(), ldapAttrStrParts.getRight());
        }
    }
}
