package gov.hhs.onc.dcdt.crypto.certs;

import java.math.BigInteger;
import javax.annotation.Nullable;

public interface CertificateConfig extends CertificateDescriptor {
    public void setCertificateAuthority(boolean ca);

    public void setCertificateType(@Nullable CertificateType certType);

    public void setSerialNumber(@Nullable BigInteger serialNum);

    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg);

    public void setSubject(@Nullable CertificateName subj);

    public void setValidInterval(@Nullable CertificateValidInterval validInterval);
}
