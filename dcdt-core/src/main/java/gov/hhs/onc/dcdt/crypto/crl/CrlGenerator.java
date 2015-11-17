package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;

public interface CrlGenerator extends CryptographyGenerator<CrlConfig, CrlInfo> {
    public CrlInfo generateCrl(PrivateKeyInfo issuerPrivateKeyInfo, AuthorityKeyIdentifier issuerAuthKeyId, CrlConfig crlConfig) throws CrlException;
}
