package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CryptographyObjectIdentifier extends CryptographyIdentifier {
    public ASN1ObjectIdentifier getOid();
}
