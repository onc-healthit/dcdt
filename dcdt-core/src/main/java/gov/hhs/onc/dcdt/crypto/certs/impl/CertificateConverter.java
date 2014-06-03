package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("certConv")
@ConvertsJson(serialize = { @Converts(from = X509Certificate.class, to = String.class) })
@ConvertsUserType(CertificateUserType.class)
@List({ @Converts(from = byte[].class, to = X509Certificate.class), @Converts(from = X509Certificate.class, to = byte[].class),
    @Converts(from = X509Certificate.class, to = String.class) })
public class CertificateConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_CERT = TypeDescriptor.valueOf(X509Certificate.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_CERT)) {
            X509Certificate sourceCert = ((X509Certificate) source);

            return (targetType.isAssignableTo(TYPE_DESC_BYTE_ARR) ? CertificateUtils.writeCertificate(sourceCert, DataEncoding.PEM) : CertificateUtils
                .certificateToString(sourceCert));
        } else {
            return CertificateUtils.readCertificate((byte[]) source, CryptographyUtils.findTypeId(CertificateType.class, targetType.getObjectType()),
                DataEncoding.PEM);
        }
    }
}
