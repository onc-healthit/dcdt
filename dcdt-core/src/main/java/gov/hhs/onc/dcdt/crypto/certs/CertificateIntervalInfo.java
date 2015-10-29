package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import java.util.Date;

public interface CertificateIntervalInfo extends CertificateIntervalDescriptor, CryptographyInfo {
    public boolean isValid();

    public boolean isValid(Date date);

    public Date getNotBefore();

    public Date getNotAfter();
}
