package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.net.utils.ToolUrlUtils;
import java.net.URL;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component("formatterUrl")
public class UrlFormatter extends AbstractToolFormatter<URL> {
    public UrlFormatter() {
        super(URL.class);
    }

    @Override
    protected URL parseInternal(String str, Locale locale) throws Exception {
        return ToolUrlUtils.fromString(str);
    }
}
