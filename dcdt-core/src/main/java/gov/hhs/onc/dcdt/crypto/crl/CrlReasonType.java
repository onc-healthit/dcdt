package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import org.bouncycastle.asn1.x509.CRLReason;

public enum CrlReasonType implements CryptographyTaggedIdentifier {
    UNSPECIFIED("unspecified", CRLReason.unspecified, false), KEY_COMPROMISE("keyCompromise", CRLReason.keyCompromise, true), CA_COMPROMISE("cACompromise",
        CRLReason.cACompromise, true), AFFILIATION_CHANGED("affiliationChanged", CRLReason.affiliationChanged, true), SUPERSEDED("superseded",
        CRLReason.superseded, true), CESSATION_OF_OPERATION("cessationOfOperation", CRLReason.cessationOfOperation, true), CERTIFICATE_HOLD("certificateHold",
        CRLReason.certificateHold, true), REMOVE_FROM_CRL("unspecified", CRLReason.removeFromCRL, false), PRIVILEGE_WITHDRAWN("privilegeWithdrawn",
        CRLReason.privilegeWithdrawn, true), AA_COMPROMISE("aACompromise", CRLReason.aACompromise, true);

    private final String id;
    private final int tag;
    private final boolean revoked;

    private CrlReasonType(String id, int tag, boolean revoked) {
        this.id = id;
        this.tag = tag;
        this.revoked = revoked;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public boolean isRevoked() {
        return this.revoked;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
