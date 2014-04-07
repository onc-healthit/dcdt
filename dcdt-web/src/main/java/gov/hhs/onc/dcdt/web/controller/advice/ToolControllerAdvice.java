package gov.hhs.onc.dcdt.web.controller.advice;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;

public interface ToolControllerAdvice<T> extends ToolBean {
    @Nullable
    public T handleException(Exception exception);
}
