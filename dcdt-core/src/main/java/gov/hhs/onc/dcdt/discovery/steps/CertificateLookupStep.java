package gov.hhs.onc.dcdt.discovery.steps;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import java.util.List;
import javax.annotation.Nullable;

public interface CertificateLookupStep<T extends ToolLookupResultBean, U extends ToolBean> extends LookupStep<T, U> {
    public boolean hasCertificateInfos();

    @Nullable
    public List<CertificateInfo> getCertificateInfos();
}
