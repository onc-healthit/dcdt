package gov.hhs.onc.dcdt.ldap.filter.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapFilterUtils;
import java.util.Locale;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.springframework.stereotype.Component;

@Component("formatterLdapFilter")
public class LdapFilterFormatter extends AbstractToolFormatter<ExprNode> {
    public LdapFilterFormatter() {
        super(ExprNode.class);
    }

    @Override
    protected String printInternal(ExprNode obj, Locale locale) throws Exception {
        return ToolLdapFilterUtils.writeFilter(obj);
    }

    @Override
    protected ExprNode parseInternal(String str, Locale locale) throws Exception {
        return ToolLdapFilterUtils.readFilter(str);
    }
}
