package gov.hhs.onc.dcdt.ldap.filter.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.filter.FilterParser;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("ldapFilterConv")
@List({ @Converts(from = String.class, to = ExprNode.class), @Converts(from = ExprNode.class, to = String.class) })
public class LdapFilterConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_EXPR_NODE = TypeDescriptor.valueOf(ExprNode.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return (sourceType.isAssignableTo(TYPE_DESC_EXPR_NODE) ? source.toString() : FilterParser.parse(((String) source)));
    }
}
