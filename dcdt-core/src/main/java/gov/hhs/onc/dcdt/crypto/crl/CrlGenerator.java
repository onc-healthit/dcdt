package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;

public interface CrlGenerator extends CryptographyGenerator<CrlConfig, CrlInfo> {
    public CrlInfo generateCrl(PrivateKeyInfo issuerPrivateKeyInfo, CrlConfig crlConfig) throws CrlException;
}
