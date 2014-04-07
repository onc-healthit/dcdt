package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.text.ParseException;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.filter.FilterParser;
import org.apache.directory.api.ldap.model.filter.ObjectClassNode;

public abstract class ToolLdapFilterUtils {
    public final static ExprNode FILTER_EQ_OBJ_CLASS_ANY = new EqualityNode<>(SchemaConstants.OBJECT_CLASS_AT, new StringValue(
        SchemaConstants.ALL_USER_ATTRIBUTES));

    public static ExprNode readFilter(String str) throws ToolLdapException {
        try {
            return FilterParser.parse(str);
        } catch (ParseException e) {
            throw new ToolLdapException(String.format("Unable to read LDAP filter from LDIF string: %s", str), e);
        }
    }

    public static String writeFilter(ExprNode filter) {
        return (ToolClassUtils.isAssignable(filter.getClass(), ObjectClassNode.class) ? FILTER_EQ_OBJ_CLASS_ANY : filter).toString();
    }
}
