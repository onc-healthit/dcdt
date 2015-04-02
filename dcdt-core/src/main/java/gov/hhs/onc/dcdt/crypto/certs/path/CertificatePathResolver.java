package gov.hhs.onc.dcdt.crypto.certs.path;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface CertificatePathResolver extends ToolBean {
    public List<CertificateInfo> resolvePath(CertificateInfo certInfo) throws CryptographyException;

    public List<CertificateInfo> resolveIssuers(CertificateInfo certInfo) throws CryptographyException;

    @Nullable
    public CertificateInfo resolveIssuer(CertificateInfo certInfo) throws CryptographyException;

    @Nonnegative
    public int getIssuerAccessLocationUrlConnectTimeout();

    public void setIssuerAccessLocationUrlConnectTimeout(@Nonnegative int issuerAccessLocUrlConnectTimeout);

    @Nonnegative
    public int getIssuerAccessLocationUrlReadTimeout();

    public void setIssuerAccessLocationUrlReadTimeout(@Nonnegative int issuerAccessLocUrlReadTimeout);
}
