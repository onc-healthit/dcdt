package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import org.bouncycastle.asn1.x509.CRLReason;

public enum CrlReasonType implements CryptographyTaggedIdentifier {
    UNSPECIFIED("unspecified", CRLReason.unspecified), KEY_COMPROMISE("keyCompromise", CRLReason.keyCompromise), CA_COMPROMISE("cACompromise",
        CRLReason.cACompromise), AFFILIATION_CHANGED("affiliationChanged", CRLReason.affiliationChanged), SUPERSEDED("superseded", CRLReason.superseded),
    CESSATION_OF_OPERATION("cessationOfOperation", CRLReason.cessationOfOperation), CERTIFICATE_HOLD("certificateHold", CRLReason.certificateHold),
    REMOVE_FROM_CRL("unspecified", CRLReason.removeFromCRL), PRIVILEGE_WITHDRAWN("privilegeWithdrawn", CRLReason.privilegeWithdrawn), AA_COMPROMISE(
        "aACompromise", CRLReason.aACompromise);

    private final String id;
    private final int tag;

    private CrlReasonType(String id, int tag) {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
