package gov.hhs.onc.dcdt.web.handler;

import gov.hhs.onc.dcdt.beans.ToolOrderedBean;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public interface ToolHandlerMethodProcessor<T, U> extends ApplicationContextAware, HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler,
    ToolOrderedBean {
    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
        throws Exception;

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception;
}
