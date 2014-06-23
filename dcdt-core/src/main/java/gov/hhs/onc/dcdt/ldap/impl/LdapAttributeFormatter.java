package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils;
import java.util.Locale;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.springframework.stereotype.Component;

@Component("formatterLdapAttr")
public class LdapAttributeFormatter extends AbstractToolFormatter<Attribute> {
    public LdapAttributeFormatter() {
        super(Attribute.class);
    }

    @Override
    protected String printInternal(Attribute obj, Locale locale) throws Exception {
        return ToolLdapAttributeUtils.writeAttribute(obj);
    }

    @Override
    protected Attribute parseInternal(String str, Locale locale) throws Exception {
        return ToolLdapAttributeUtils.readAttribute(str);
    }
}
