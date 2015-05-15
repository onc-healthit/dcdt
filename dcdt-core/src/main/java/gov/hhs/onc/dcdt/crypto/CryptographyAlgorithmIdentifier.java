package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CryptographyAlgorithmIdentifier extends CryptographyObjectIdentifier {
    public final static String PROP_NAME_ALG_ID = "algId";

    public AlgorithmIdentifier getAlgorithmId();
}
