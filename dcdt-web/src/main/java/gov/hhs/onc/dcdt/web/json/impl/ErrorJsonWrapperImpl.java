package gov.hhs.onc.dcdt.web.json.impl;


import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.web.json.ErrorJsonWrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;

@JsonTypeName("error")
public class ErrorJsonWrapperImpl extends AbstractToolBean implements ErrorJsonWrapper {
    private String msg;
    private String stackTrace;

    public ErrorJsonWrapperImpl() {
    }

    public ErrorJsonWrapperImpl(Throwable error) {
        this.msg = error.getMessage();
        this.stackTrace = ExceptionUtils.getStackTrace(error);
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public void setMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String getStackTrace() {
        return this.stackTrace;
    }

    @Override
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
