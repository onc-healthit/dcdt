package gov.hhs.onc.dcdt.discovery.steps;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

public interface CertificateLookupStep<T extends Serializable, U extends Enum<U>, V extends ToolLookupResultBean<T, U>, W extends ToolBean> extends
    LookupStep<T, U, V, W> {
    public boolean hasCertificateInfos();
    
    @Nullable
    public List<CertificateInfo> getCertificateInfos();
}
