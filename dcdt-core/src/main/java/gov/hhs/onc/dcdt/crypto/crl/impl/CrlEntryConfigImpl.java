package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.crl.CrlEntryConfig;
import gov.hhs.onc.dcdt.crypto.crl.CrlReasonType;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Nullable;

public class CrlEntryConfigImpl extends AbstractCrlEntryDescriptor implements CrlEntryConfig {
    @Override
    public void setRevocationDate(@Nullable Date revocationDate) {
        this.revocationDate = revocationDate;
    }

    @Override
    public void setRevocationReason(@Nullable CrlReasonType revocationReason) {
        this.revocationReason = revocationReason;
    }

    @Override
    public void setSerialNumber(@Nullable BigInteger serialNum) {
        this.serialNum = serialNum;
    }
}
