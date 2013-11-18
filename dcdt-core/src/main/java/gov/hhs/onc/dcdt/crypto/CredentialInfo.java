package gov.hhs.onc.dcdt.crypto;

public interface CredentialInfo {

    public CertificateInfo getCertificateInfo();

    public void setCertificateInfo(CertificateInfo certificateInfo);

    public KeyPairInfo getKeyPairInfo();

    public void setKeyPairInfo(KeyPairInfo keyPairInfo);

}
