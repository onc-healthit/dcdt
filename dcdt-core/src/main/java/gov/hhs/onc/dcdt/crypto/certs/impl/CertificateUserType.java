package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractBlobUserType;
import java.security.cert.X509Certificate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("certUserType")
@Scope("singleton")
public class CertificateUserType extends AbstractBlobUserType<X509Certificate> {
    public CertificateUserType() {
        super(X509Certificate.class);
    }
}
