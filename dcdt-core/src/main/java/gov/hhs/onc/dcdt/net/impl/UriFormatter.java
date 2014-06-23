package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.net.URI;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component("formatterUri")
public class UriFormatter extends AbstractToolFormatter<URI> {
    public UriFormatter() {
        super(URI.class);
    }

    @Override
    protected URI parseInternal(String str, Locale locale) throws Exception {
        return new URI(str);
    }
}
