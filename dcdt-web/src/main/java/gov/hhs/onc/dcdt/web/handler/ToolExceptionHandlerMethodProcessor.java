package gov.hhs.onc.dcdt.web.handler;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public interface ToolExceptionHandlerMethodProcessor<T, U, V extends Throwable> extends HandlerExceptionResolver, ToolHandlerMethodProcessor<T, U> {
    @Nullable
    @Override
    public ModelAndView
        resolveException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler, Exception exception);

    public boolean supportsException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler, Exception exception);
}
