package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import java.net.URL;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("urlUserType")
@Scope("singleton")
public class UrlUserType extends AbstractStringUserType<URL> {
    public UrlUserType() {
        super(URL.class);
    }
}
