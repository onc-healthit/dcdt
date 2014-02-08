package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.LdapAssertionType;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.filter.AndNode;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.filter.PresenceNode;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("exprNodeConv")
@List({ @Converts(from = String.class, to = ExprNode.class) })
public class ExprNodeConverter extends AbstractToolConverter {
    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        String sourceStr =
            StringUtils.removePattern(StringUtils.removePattern((String) source, ToolLdapAttributeUtils.PATTERN_STR_ATTR_BEFORE),
                ToolLdapAttributeUtils.PATTERN_STR_ATTR_AFTER);
        String[] sourceLdapAssertStrs = ToolLdapAttributeUtils.PATTERN_ATTR_DELIM.split(sourceStr);
        java.util.List<ExprNode> targetExprNodes = new ArrayList<>(sourceLdapAssertStrs.length);
        Matcher sourceLdapAssertMatcher;

        for (String sourceLdapAssertStr : sourceLdapAssertStrs) {
            if ((sourceLdapAssertMatcher = LdapAssertionType.PRESENCE.getPattern().matcher(sourceLdapAssertStr)).matches()) {
                targetExprNodes.add(new PresenceNode(sourceLdapAssertMatcher.group(1)));
            } else if ((sourceLdapAssertMatcher = LdapAssertionType.EQUALITY.getPattern().matcher(sourceLdapAssertStr)).matches()) {
                targetExprNodes.add(new EqualityNode<>(sourceLdapAssertMatcher.group(1), new StringValue(sourceLdapAssertMatcher.group(2))));
            }
        }

        return new AndNode(targetExprNodes);
    }
}
