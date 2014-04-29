package gov.hhs.onc.dcdt.discovery.steps.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.LookupStep;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public abstract class AbstractLookupStep<T extends Serializable, U extends Enum<U>, V extends ToolLookupResultBean<T, U>, W extends ToolBean> extends
    AbstractCertificateDiscoveryStep implements LookupStep<T, U, V, W> {
    protected W lookupService;
    protected V result;

    protected AbstractLookupStep(BindingType bindingType, @Nullable LocationType locType, W lookupService) {
        super(bindingType, locType);

        this.lookupService = lookupService;
    }

    @Override
    public boolean execute(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        return (((this.result = this.executeLookup(prevSteps, directAddr)) != null) && this.isSuccess());
    }

    @Override
    public boolean isSuccess() {
        return (super.isSuccess() && this.hasResult() && this.result.isSuccess());
    }

    @Nullable
    protected abstract V executeLookup(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr);

    @Override
    public W getLookupService() {
        return this.lookupService;
    }

    @Override
    public boolean hasResult() {
        return (this.result != null);
    }

    @Nullable
    @Override
    public V getResult() {
        return this.result;
    }

    @Override
    protected Object[] getCloneArguments() {
        return ArrayUtils.toArray(this.bindingType, this.lookupService);
    }
}
