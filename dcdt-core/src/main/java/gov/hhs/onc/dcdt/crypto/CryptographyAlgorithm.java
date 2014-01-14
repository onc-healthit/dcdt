package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CryptographyAlgorithm extends CryptographyObjectIdentifier {
    public AlgorithmIdentifier getId();
}
