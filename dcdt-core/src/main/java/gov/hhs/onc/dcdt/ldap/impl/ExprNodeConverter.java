package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.LdapAssertionType;
import java.util.regex.Matcher;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("exprNodeConv")
@List({ @Converts(from = String.class, to = ExprNode.class) })
@Scope("singleton")
public class ExprNodeConverter extends AbstractToolConverter {
    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        Matcher sourceLdapAssertionMatcher = LdapAssertionType.EQUALITY.getPattern().matcher((String) source);

        return sourceLdapAssertionMatcher.matches() ? new EqualityNode<>(sourceLdapAssertionMatcher.group(1), new StringValue(
            sourceLdapAssertionMatcher.group(2))) : null;
    }
}
