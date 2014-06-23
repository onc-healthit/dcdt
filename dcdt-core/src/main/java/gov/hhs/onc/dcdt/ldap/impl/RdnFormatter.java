package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.ldap.utils.ToolRdnUtils;
import java.util.Locale;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.stereotype.Component;

@Component("formatterRdn")
public class RdnFormatter extends AbstractToolFormatter<Rdn> {
    public RdnFormatter() {
        super(Rdn.class);
    }

    @Override
    protected Rdn parseInternal(String str, Locale locale) throws Exception {
        return ToolRdnUtils.fromString(str);
    }
}
