package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStepDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultType;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseResultStep extends AbstractToolBean implements ToolTestcaseResultStep {
    protected ToolTestcaseResultStepDescription description;
    protected ToolTestcaseResultType resultType;
    protected boolean successful;
    protected String message;

    @Override
    public boolean hasDescription() {
        return this.description != null;
    }

    @Nullable
    @Override
    public ToolTestcaseResultStepDescription getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(@Nullable ToolTestcaseResultStepDescription description) {
        this.description = description;
    }

    @Override
    public ToolTestcaseResultType getResultType() {
        return this.resultType;
    }

    @Override
    public void setResultType(ToolTestcaseResultType resultType) {
        this.resultType = resultType;
    }

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
}
