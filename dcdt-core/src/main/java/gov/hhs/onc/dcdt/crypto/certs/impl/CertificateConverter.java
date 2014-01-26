package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("certConv")
@ConvertsUserType(CertificateUserType.class)
@List({ @Converts(from = byte[].class, to = X509Certificate.class), @Converts(from = X509Certificate.class, to = byte[].class) })
@Scope("singleton")
public class CertificateConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_CERT = TypeDescriptor.valueOf(X509Certificate.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return (sourceType.isAssignableTo(TYPE_DESC_CERT)) ? CertificateUtils.writeCertificate((X509Certificate) source, DataEncoding.PEM) : CertificateUtils
            .readCertificate((byte[]) source, CryptographyUtils.findTypeId(CertificateType.class, targetType.getObjectType()), DataEncoding.PEM);
    }
}
