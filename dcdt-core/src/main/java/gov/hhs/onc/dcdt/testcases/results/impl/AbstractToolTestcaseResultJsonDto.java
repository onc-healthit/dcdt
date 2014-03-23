package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import javax.annotation.Nullable;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolTestcaseResultJsonDto<T extends ToolTestcaseResult> extends AbstractToolBeanJsonDto<T> implements
    ToolTestcaseResultJsonDto<T> {
    protected boolean successful;
    protected String message;
    protected String certStr;
    protected List<ToolTestcaseStep> infoSteps;

    protected AbstractToolTestcaseResultJsonDto(Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(beanClass, beanImplClass);
    }

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public String getCertificate() {
        return this.certStr;
    }

    @Override
    public void setCertificate(@Nullable String certStr) {
        this.certStr = certStr;
    }

    @Nullable
    @Override
    public List<ToolTestcaseStep> getInfoSteps() {
        return this.infoSteps;
    }

    @Override
    public void setInfoSteps(@Nullable List<ToolTestcaseStep> infoSteps) {
        this.infoSteps = infoSteps;
    }
}
