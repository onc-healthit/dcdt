package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import java.math.BigInteger;
import java.util.Map;
import javax.annotation.Nullable;

public interface CrlDescriptor<T extends CrlEntryDescriptor> extends CryptographyDescriptor {
    public boolean hasCrlType();

    @Nullable
    public CrlType getCrlType();

    public boolean hasEntries();

    public Map<BigInteger, T> getEntries();

    public boolean hasIssuerDn();

    @Nullable
    public CertificateDn getIssuerDn();

    public boolean hasNumber();

    @Nullable
    public BigInteger getNumber();

    public boolean hasSignatureAlgorithm();

    @Nullable
    public SignatureAlgorithm getSignatureAlgorithm();
}
