package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CryptographyObjectIdentifier extends CryptographyIdentifier {
    public final static String PROP_NAME_OID = "oid";

    public ASN1ObjectIdentifier getOid();
}
