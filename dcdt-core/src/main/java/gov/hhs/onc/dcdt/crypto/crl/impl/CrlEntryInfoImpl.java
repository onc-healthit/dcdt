package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.crl.CrlEntryInfo;
import gov.hhs.onc.dcdt.crypto.crl.CrlException;
import gov.hhs.onc.dcdt.crypto.crl.CrlReasonType;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509CrlEntry;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Optional;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public class CrlEntryInfoImpl extends AbstractCrlEntryDescriptor implements CrlEntryInfo {
    private ToolX509CrlEntry crlEntry;

    public CrlEntryInfoImpl() throws CrlException {
        this(null);
    }

    public CrlEntryInfoImpl(@Nullable ToolX509CrlEntry crlEntry) throws CrlException {
        this.setCrlEntry(crlEntry);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        CrlEntryInfo crlEntryInfo;

        return (this.hasCrlEntry() && (obj != null) && ToolClassUtils.isAssignable(obj.getClass(), CrlEntryInfo.class)
            && (crlEntryInfo = ((CrlEntryInfo) obj)).hasCrlEntry() && this.crlEntry.equals(crlEntryInfo.getCrlEntry()));
    }

    @Override
    public int hashCode() {
        return (this.hasCrlEntry() ? this.crlEntry.hashCode() : super.hashCode());
    }

    @Override
    protected void reset() {
        super.reset();

        this.crlEntry = null;
    }

    private void processCrlEntry() throws CrlException {
        this.reset();

        if (!this.hasCrlEntry()) {
            return;
        }

        try {
            this.revocationDate = this.crlEntry.getRevocationDate();
            this.revocationReason =
                Optional
                    .ofNullable(this.getExtension(Extension.reasonCode))
                    .map(
                        revocationReasonExt -> CryptographyUtils.findByTag(CrlReasonType.class, CRLReason.getInstance(revocationReasonExt.getParsedValue())
                            .getValue().intValue())).orElse(null);
            this.serialNum = this.crlEntry.getSerialNumber();
        } catch (Exception e) {
            this.reset();

            throw e;
        }
    }

    @Override
    public boolean hasCrlEntry() {
        return (this.crlEntry != null);
    }

    @Nullable
    @Override
    public ToolX509CrlEntry getCrlEntry() {
        return this.crlEntry;
    }

    @Override
    public void setCrlEntry(@Nullable ToolX509CrlEntry crlEntry) throws CrlException {
        this.crlEntry = crlEntry;

        this.processCrlEntry();
    }

    @Override
    public boolean hasExtension(ASN1ObjectIdentifier oid) {
        return (this.getExtension(oid) != null);
    }

    @Nullable
    @Override
    public Extension getExtension(ASN1ObjectIdentifier oid) {
        // noinspection ConstantConditions
        return (this.hasExtensions() ? this.getExtensions().getExtension(oid) : null);
    }

    @Override
    public boolean hasExtensions() {
        return (this.hasCrlEntry() && this.crlEntry.hasExtensions());
    }

    @Nullable
    @Override
    public Extensions getExtensions() {
        return (this.hasExtensions() ? this.crlEntry.getEntry().getExtensions() : null);
    }
}
