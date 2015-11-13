package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convCert")
@Converts({ @Convert(from = byte[].class, to = X509Certificate.class), @Convert(from = X509Certificate.class, to = byte[].class) })
public class CertificateConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_CERT = TypeDescriptor.valueOf(X509Certificate.class);

    @Nullable
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        return (srcType.isAssignableTo(TYPE_DESC_CERT) ? CertificateUtils.writeCertificate(((X509Certificate) src), DataEncoding.PEM) : CertificateUtils
            .readCertificate(((byte[]) src), ToolEnumUtils.findByType(CertificateType.class, targetType.getObjectType()), DataEncoding.PEM));
    }
}
