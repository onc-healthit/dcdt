package gov.hhs.onc.dcdt.web.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.web.json.ErrorJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ErrorsJsonWrapper;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@JsonTypeName("errors")
public class ErrorsJsonWrapperImpl extends AbstractToolBean implements ErrorsJsonWrapper {
    private List<ErrorJsonWrapper> globalErrors;
    private Map<String, List<ErrorJsonWrapper>> fieldErrors;

    @Override
    public boolean hasErrors() {
        return this.hasGlobalErrors() || this.hasFieldErrors();
    }

    @Override
    public boolean hasFieldErrors(String fieldName) {
        return this.hasFieldErrors() && this.fieldErrors.containsKey(fieldName);
    }

    @Override
    public boolean hasFieldErrors() {
        return (this.fieldErrors != null);
    }

    @Nullable
    @Override
    public List<ErrorJsonWrapper> getFieldErrors(String fieldName) {
        return this.hasFieldErrors() ? this.fieldErrors.get(fieldName) : null;
    }

    @Nullable
    @Override
    public Map<String, List<ErrorJsonWrapper>> getFieldErrors() {
        return this.fieldErrors;
    }

    @Override
    public void setFieldErrors(@Nullable Map<String, List<ErrorJsonWrapper>> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    @Override
    public boolean hasGlobalErrors() {
        return (this.globalErrors != null);
    }

    @Nullable
    @Override
    public List<ErrorJsonWrapper> getGlobalErrors() {
        return this.globalErrors;
    }

    @Override
    public void setGlobalErrors(@Nullable List<ErrorJsonWrapper> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
