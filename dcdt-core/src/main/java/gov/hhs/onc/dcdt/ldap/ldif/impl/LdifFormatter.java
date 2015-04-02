package gov.hhs.onc.dcdt.ldap.ldif.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdifUtils;
import java.util.Locale;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.springframework.stereotype.Component;

@Component("formatterLdif")
public class LdifFormatter extends AbstractToolFormatter<LdifEntry> {
    public LdifFormatter() {
        super(LdifEntry.class);
    }

    @Override
    protected String printInternal(LdifEntry obj, Locale locale) throws Exception {
        return ToolLdifUtils.writeEntry(obj);
    }

    @Override
    protected LdifEntry parseInternal(String str, Locale locale) throws Exception {
        return ToolLdifUtils.readEntry(str);
    }
}
