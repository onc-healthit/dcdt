package gov.hhs.onc.dcdt.web.handler.impl;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

public class ToolExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {
    @Override
    public ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
        return super.getExceptionHandlerMethod(handlerMethod, exception);
    }
}
