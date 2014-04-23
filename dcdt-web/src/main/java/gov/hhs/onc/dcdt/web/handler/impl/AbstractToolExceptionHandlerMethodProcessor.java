package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.handler.ToolExceptionHandlerMethodProcessor;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolExceptionHandlerMethodProcessor<T, U, V extends Exception> extends AbstractToolHandlerMethodProcessor<T, U> implements
    ToolExceptionHandlerMethodProcessor<T, U, V> {
    protected Class<? extends V> targetExceptionClass;
    protected boolean resolveExceptions;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolExceptionHandlerMethodProcessor.class);

    protected AbstractToolExceptionHandlerMethodProcessor(Class<? extends T> targetArgsClass, Class<? extends U> targetReturnTypeClass,
        Class<? extends V> targetExceptionClass, boolean resolveArgs, boolean handleReturnValue, boolean resolveExceptions) {
        super(targetArgsClass, targetReturnTypeClass, resolveArgs, handleReturnValue);

        this.targetExceptionClass = targetExceptionClass;
        this.resolveExceptions = resolveExceptions;
    }

    @Nullable
    @Override
    public ModelAndView
        resolveException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler, Exception exception) {
        if (this.supportsException(servletReq, servletResp, exceptionHandler, exception)) {
            ModelAndView exceptionModelAndView =
                this.resolveExceptionInternal(servletReq, servletResp, exceptionHandler, this.targetExceptionClass.cast(exception));

            LOGGER.trace(String.format("Processed (class=%s) exception (class=%s) handler method (class=%s).", ToolClassUtils.getName(this),
                ToolClassUtils.getName(exception), ToolClassUtils.getName(exceptionHandler)));

            return exceptionModelAndView;
        } else {
            return null;
        }
    }

    @Override
    public boolean supportsException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler, Exception exception) {
        return this.resolveExceptions && ToolClassUtils.isAssignable(ToolClassUtils.getClass(exception), this.targetExceptionClass);
    }

    @Nullable
    protected ModelAndView resolveExceptionInternal(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler,
        V exception) {
        return null;
    }
}
