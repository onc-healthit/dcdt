package gov.hhs.onc.dcdt.crypto.impl;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import gov.hhs.onc.dcdt.crypto.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.CertificateName;
import gov.hhs.onc.dcdt.crypto.CertificateValidInterval;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;

public class CertificateInfoImpl implements CertificateInfo {

    private BigInteger serialNumber;
    private byte[] signature;
    private String sigAlgName;
    private CredentialInfo issuerCredentials;
    private CertificateName subject;
    private CertificateValidInterval validInterval;
    private SubjectPublicKeyInfo publicKey;
    private String dataEncoding;
    private boolean isCa;

    @Override
    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public byte[] getSignature() {
        return this.signature;
    }

    @Override
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public String getSigAlgName() {
        return this.sigAlgName;
    }

    @Override
    public void setSigAlgName(String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    @Override
    public CredentialInfo getIssuerCredentials() {
        return this.issuerCredentials;
    }

    @Override
    public void setIssuerCredentials(CredentialInfo issuerCredentials) {
        this.issuerCredentials = issuerCredentials;
    }

    @Override
    public CertificateName getIssuer() {
        return this.issuerCredentials != null ? this.getIssuerCredentials().getCertificateInfo().getSubject() : this.getSubject();
    }

    @Override
    public PrivateKey getIssuerPrivateKey() {
        return this.getIssuerCredentials() != null ? this.getIssuerCredentials().getKeyPairInfo().getPrivateKey() : null;
    }

    @Override
    public CertificateName getSubject() {
        return this.subject;
    }

    @Override
    public void setSubject(CertificateName subject) {
        this.subject = subject;
    }

    @Override
    public CertificateValidInterval getValidInterval() {
        return this.validInterval;
    }

    @Override
    public void setValidInterval(CertificateValidInterval validInterval) {
        this.validInterval = validInterval;
    }

    @Override
    public SubjectPublicKeyInfo getPublicKey() {
        return this.publicKey;
    }

    @Override
    public void setPublicKey(SubjectPublicKeyInfo publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String getDataEncoding() {
        return this.dataEncoding;
    }

    @Override
    public void setDataEncoding(String dataEncoding) {
        this.dataEncoding = dataEncoding;
    }

    @Override
    public AuthorityKeyIdentifier getAuthKeyId() {
        if (this.getIssuerCredentials() != null) {
            PublicKey issuerPublicKey = this.getIssuerCredentials().getKeyPairInfo().getPublicKey();
            return new AuthorityKeyIdentifier(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(issuerPublicKey.getEncoded())));
        }
        return null;
    }

    @Override
    public SubjectKeyIdentifier getSubjKeyId() {
        if (this.getPublicKey() != null) {
            Digest subjKeyDigest = new SHA1Digest();
            byte[] subjKeyIdData = new byte[subjKeyDigest.getDigestSize()];
            byte[] pubKeyData = this.getPublicKey().getPublicKeyData().getBytes();

            subjKeyDigest.update(pubKeyData, 0, pubKeyData.length);
            subjKeyDigest.doFinal(subjKeyIdData, 0);

            return new SubjectKeyIdentifier(subjKeyIdData);
        }

        return null;
    }

    @Override
    public boolean getIsCa() {
        return this.isCa;
    }

    @Override
    public void setIsCa(boolean isCa) {
        this.isCa = isCa;
    }

}
