package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CryptographyObjectIdentifier extends ToolIdentifier {
    public ASN1ObjectIdentifier getOid();
}
