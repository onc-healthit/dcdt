package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStepDescription;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultType;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseResultStep extends AbstractToolBean implements ToolTestcaseResultStep {
    protected ToolTestcaseResultStepDescription description;
    protected ToolTestcaseResultType resultType;
    protected BindingType bindingType;
    protected LocationType locationType;
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
    public BindingType getBindingType() {
        return this.bindingType;
    }

    @Override
    public void setBindingType(BindingType bindingType) {
        this.bindingType = bindingType;
    }

    @Override
    public LocationType getLocationType() {
        return this.locationType;
    }

    @Override
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
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
