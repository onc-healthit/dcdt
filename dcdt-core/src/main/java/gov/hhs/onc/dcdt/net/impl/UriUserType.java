package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import java.net.URI;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("uriUserType")
@Scope("singleton")
public class UriUserType extends AbstractStringUserType<URI> {
    public UriUserType() {
        super(URI.class);
    }
}
