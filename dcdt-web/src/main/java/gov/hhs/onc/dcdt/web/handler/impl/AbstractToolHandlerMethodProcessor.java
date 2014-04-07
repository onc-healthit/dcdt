package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolOrderedBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.handler.ToolHandlerMethodProcessor;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolHandlerMethodProcessor<T, U> extends AbstractToolOrderedBean implements ToolHandlerMethodProcessor<T, U> {
    protected AbstractApplicationContext appContext;
    protected Class<? extends T> targetArgsClass;
    protected Class<? extends U> targetReturnTypeClass;
    protected boolean resolveArgs;
    protected boolean handleReturnValue;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolHandlerMethodProcessor.class);

    protected AbstractToolHandlerMethodProcessor(Class<? extends T> targetArgsClass, Class<? extends U> targetReturnTypeClass, boolean resolveArgs,
        boolean handleReturnValue) {
        this.targetArgsClass = targetArgsClass;
        this.targetReturnTypeClass = targetReturnTypeClass;
        this.resolveArgs = resolveArgs;
        this.handleReturnValue = handleReturnValue;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return this.handleReturnValue && ToolClassUtils.isAssignable(returnType.getParameterType(), this.targetReturnTypeClass);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParam) {
        return this.resolveArgs && ToolClassUtils.isAssignable(methodParam.getParameterType(), this.targetArgsClass);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
        throws Exception {
        this.handleReturnValueInternal(this.targetReturnTypeClass.cast(returnValue), returnType, mavContainer, webRequest);

        LOGGER.trace(String.format("Processed (class=%s) handler method (class=%s, name=%s) return value (class=%s, targetClass=%s).",
            ToolClassUtils.getName(this), ToolClassUtils.getName(returnType.getContainingClass()), returnType.getMethod().getName(),
            ToolClassUtils.getName(returnValue), ToolClassUtils.getName(this.targetReturnTypeClass)));
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        Object argObj = this.resolveArgumentInternal(methodParam, mavContainer, webRequest, binderFactory);

        LOGGER.trace(String.format("Processed (class=%s) handler method (class=%s, name=%s) parameter (class=%s, targetClass=%s).",
            ToolClassUtils.getName(this), ToolClassUtils.getName(methodParam.getContainingClass()), methodParam.getMethod().getName(),
            ToolClassUtils.getName(argObj), ToolClassUtils.getName(this.targetArgsClass)));

        return argObj;
    }

    protected void handleReturnValueInternal(@Nullable U returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest) throws Exception {
    }

    @Nullable
    protected Object resolveArgumentInternal(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
