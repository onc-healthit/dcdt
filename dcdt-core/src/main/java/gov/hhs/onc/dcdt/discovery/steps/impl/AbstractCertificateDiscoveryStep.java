package gov.hhs.onc.dcdt.discovery.steps.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolResultBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStepDescription;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractCertificateDiscoveryStep extends AbstractToolResultBean implements CertificateDiscoveryStep {
    protected AbstractApplicationContext appContext;
    protected BindingType bindingType;
    protected CertificateDiscoveryStepDescription desc;
    protected LocationType locType;
    protected List<String> execMsgs = new ArrayList<>();
    protected Boolean execSuccess;

    protected AbstractCertificateDiscoveryStep(BindingType bindingType, LocationType locType) {
        this.bindingType = bindingType;
        this.locType = locType;
    }

    @Override
    public List<String> getMessages() {
        return new ArrayList<>(this.execMsgs);
    }

    @Override
    public boolean isSuccess() {
        return this.isExecutionSuccess();
    }

    @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone" })
    public Object clone() throws CloneNotSupportedException {
        return ToolBeanFactoryUtils.createBean(this.appContext, this.beanName, this.getClass(), this.getCloneArguments());
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public BindingType getBindingType() {
        return this.bindingType;
    }

    @Override
    public boolean hasDescription() {
        return (this.desc != null);
    }

    @Nullable
    @Override
    public CertificateDiscoveryStepDescription getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable CertificateDiscoveryStepDescription desc) {
        this.desc = desc;
    }

    @Override
    public boolean hasExecutionMessages() {
        return !this.execMsgs.isEmpty();
    }

    @Override
    public List<String> getExecutionMessages() {
        return this.execMsgs;
    }

    @Override
    public boolean isExecutionSuccess() {
        return ObjectUtils.defaultIfNull(this.execSuccess, true);
    }

    @Override
    public void setExecutionSuccess(boolean execSuccess) {
        this.execSuccess = execSuccess;
    }

    @Override
    public LocationType getLocationType() {
        return this.locType;
    }

    protected Object[] getCloneArguments() {
        return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }
}
