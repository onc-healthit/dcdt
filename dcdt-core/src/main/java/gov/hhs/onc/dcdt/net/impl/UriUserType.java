package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import java.net.URI;
import org.springframework.stereotype.Component;

@Component("uriUserType")
public class UriUserType extends AbstractStringUserType<URI> {
    public UriUserType() {
        super(URI.class);
    }
}
