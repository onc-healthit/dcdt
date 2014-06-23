package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.ldap.utils.ToolDnUtils;
import java.util.Locale;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.name.Dn;
import org.springframework.stereotype.Component;

@Component("formatterDn")
public class DnFormatter extends AbstractToolFormatter<Dn> {
    public DnFormatter() {
        super(Dn.class);
    }

    @Nullable
    @Override
    protected Dn parseInternal(String str, Locale locale) throws Exception {
        return ToolDnUtils.fromStrings(str);
    }
}
