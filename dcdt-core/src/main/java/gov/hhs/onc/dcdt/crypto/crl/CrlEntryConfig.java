package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Nullable;

public interface CrlEntryConfig extends CrlEntryDescriptor, CryptographyConfig {
    public void setRevocationDate(@Nullable Date revocationDate);

    public void setRevocationReason(@Nullable CrlReasonType revocationReason);

    public void setSerialNumber(@Nullable BigInteger serialNum);
}
