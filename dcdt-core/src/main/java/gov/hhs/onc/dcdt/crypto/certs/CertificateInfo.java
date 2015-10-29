package gov.hhs.onc.dcdt.crypto.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;

@JsonTypeInfo(use = Id.NONE)
public interface CertificateInfo extends CertificateDescriptor<CertificateIntervalInfo>, CryptographyInfo {
    public boolean hasCertificate();

    @JsonProperty("cert")
    @Nullable
    public X509Certificate getCertificate();

    public void setCertificate(@Nullable X509Certificate cert) throws CertificateException;

    public boolean hasCertificateHolder();

    @Nullable
    public X509CertificateHolder getCertificateHolder();

    public boolean hasExtension(ASN1ObjectIdentifier oid);

    @Nullable
    public Extension getExtension(ASN1ObjectIdentifier oid);

    public boolean hasExtensions();

    @Nullable
    public Extensions getExtensions();

    public boolean hasIssuerAltNames();

    @Nullable
    public ToolMultiValueMap<GeneralNameType, ASN1Encodable> getIssuerAltNames();

    public boolean hasIssuerDn();

    @Nullable
    public CertificateDn getIssuerDn();
}
