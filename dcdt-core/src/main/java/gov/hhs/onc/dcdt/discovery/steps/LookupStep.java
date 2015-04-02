package gov.hhs.onc.dcdt.discovery.steps;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import java.io.Serializable;
import javax.annotation.Nullable;

public interface LookupStep<T extends Serializable, U extends Enum<U>, V extends ToolLookupResultBean<T, U>, W extends ToolBean> extends
    CertificateDiscoveryStep {
    public W getLookupService();

    public boolean hasResult();

    @Nullable
    public V getResult();
}
