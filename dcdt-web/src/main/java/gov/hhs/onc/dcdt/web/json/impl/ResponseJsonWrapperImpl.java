package gov.hhs.onc.dcdt.web.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.ErrorsJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseStatus;
import javax.annotation.Nullable;

@JsonTypeName("response")
public class ResponseJsonWrapperImpl<T extends ToolBean> extends AbstractJsonWrapper<T> implements ResponseJsonWrapper<T> {
    private ResponseStatus status = ResponseStatus.SUCCESS;
    private ErrorsJsonWrapper errors;

    @Nullable
    @Override
    public ErrorsJsonWrapper getErrors() {
        return this.errors;
    }

    @Override
    public void setErrors(@Nullable ErrorsJsonWrapper errors) {
        this.errors = errors;
    }

    public ResponseStatus getStatus() {
        return this.status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
