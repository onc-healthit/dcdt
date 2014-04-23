package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.math.BigInteger;

public interface CertificateSerialNumber extends ToolBean {
    public BigInteger getValue();
}
