package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.crl.CrlEntryDescriptor;
import gov.hhs.onc.dcdt.crypto.crl.CrlReasonType;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Nullable;

public abstract class AbstractCrlEntryDescriptor extends AbstractCryptographyDescriptor implements CrlEntryDescriptor {
    protected Date revocationDate;
    protected CrlReasonType revocationReason;
    protected BigInteger serialNum;

    @Override
    protected void reset() {
        this.revocationDate = null;
        this.revocationReason = null;
        this.serialNum = null;
    }

    @Override
    public boolean hasRevocationDate() {
        return (this.revocationDate != null);
    }

    @Override
    public Date getRevocationDate() {
        return this.revocationDate;
    }

    @Override
    public boolean hasRevocationReason() {
        return ((this.revocationReason != null) && (this.revocationReason != CrlReasonType.UNSPECIFIED));
    }

    @Nullable
    @Override
    public CrlReasonType getRevocationReason() {
        return this.revocationReason;
    }

    @Override
    public boolean hasSerialNumber() {
        return (this.serialNum != null);
    }

    @Nullable
    @Override
    public BigInteger getSerialNumber() {
        return this.serialNum;
    }
}
