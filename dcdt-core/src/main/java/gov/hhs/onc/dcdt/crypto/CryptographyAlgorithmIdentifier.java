package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CryptographyAlgorithmIdentifier extends CryptographyObjectIdentifier {
    public AlgorithmIdentifier getAlgorithmId();
}
