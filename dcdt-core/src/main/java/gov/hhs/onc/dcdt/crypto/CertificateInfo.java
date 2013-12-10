package gov.hhs.onc.dcdt.crypto;

import java.math.BigInteger;
import java.security.PrivateKey;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public interface CertificateInfo {
    public BigInteger getSerialNumber();

    public void setSerialNumber(BigInteger serialNumber);

    public byte[] getSignature();

    public void setSignature(byte[] signature);

    public String getSigAlgName();

    public void setSigAlgName(String sigAlgName);

    public CredentialInfo getIssuerCredentials();

    public void setIssuerCredentials(CredentialInfo issuerCredentials);

    public CertificateName getIssuer();

    public PrivateKey getIssuerPrivateKey();

    public CertificateName getSubject();

    public void setSubject(CertificateName subject);

    public CertificateValidInterval getValidInterval();

    public void setValidInterval(CertificateValidInterval validInterval);

    public SubjectPublicKeyInfo getPublicKey();

    public void setPublicKey(SubjectPublicKeyInfo publicKey);

    public String getDataEncoding();

    public void setDataEncoding(String dataEncoding);

    public AuthorityKeyIdentifier getAuthKeyId();

    public SubjectKeyIdentifier getSubjKeyId();

    public boolean getIsCa();

    public void setIsCa(boolean isCa);
}
