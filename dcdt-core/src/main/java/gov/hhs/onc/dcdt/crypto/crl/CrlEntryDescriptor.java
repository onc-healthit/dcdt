package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Nullable;

public interface CrlEntryDescriptor extends CryptographyDescriptor {
    public boolean hasRevocationDate();

    public Date getRevocationDate();

    public boolean hasRevocationReason();

    @Nullable
    public CrlReasonType getRevocationReason();

    public boolean hasSerialNumber();

    @Nullable
    public BigInteger getSerialNumber();
}
