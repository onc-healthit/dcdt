package gov.hhs.onc.dcdt.discovery.steps;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import javax.annotation.Nullable;

public interface LookupStep<T extends ToolLookupResultBean, U extends ToolBean> extends CertificateDiscoveryStep {
    public U getLookupService();

    public boolean hasResult();

    @Nullable
    public T getResult();
}
