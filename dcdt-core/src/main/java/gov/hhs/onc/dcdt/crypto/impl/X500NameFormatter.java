package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.util.Locale;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.stereotype.Component;

@Component("formatterX500Name")
public class X500NameFormatter extends AbstractToolFormatter<X500Name> {
    public X500NameFormatter() {
        super(X500Name.class);
    }

    @Override
    protected X500Name parseInternal(String str, Locale locale) throws Exception {
        return new X500Name(str);
    }
}
