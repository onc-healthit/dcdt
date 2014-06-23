package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.security.cert.X509Certificate;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component("formatterCert")
public class CertificateFormatter extends AbstractToolFormatter<X509Certificate> {
    public CertificateFormatter() {
        super(X509Certificate.class, false, true);
    }

    @Override
    protected String printInternal(X509Certificate obj, Locale locale) throws Exception {
        return CertificateUtils.readCertificate(CryptographyUtils.JCE_PROVIDER_HELPER, obj.getEncoded(), CertificateType.X509, DataEncoding.DER).toString();
    }
}
