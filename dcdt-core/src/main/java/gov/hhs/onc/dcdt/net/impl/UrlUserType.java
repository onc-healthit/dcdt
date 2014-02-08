package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component("urlUserType")
public class UrlUserType extends AbstractStringUserType<URL> {
    public UrlUserType() {
        super(URL.class);
    }
}
