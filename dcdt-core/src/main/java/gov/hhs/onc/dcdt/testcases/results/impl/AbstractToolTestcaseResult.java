package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractToolTestcaseResult extends AbstractToolNamedBean implements ToolTestcaseResult {
    protected boolean successful;
    protected String message;
    protected String certStr;
    protected List<ToolTestcaseStep> infoSteps;

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.message);
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

    @Override
    public boolean hasInfoSteps() {
        return this.infoSteps != null;
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
