package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Component("exceptionHandlerExceptionResolverBeanPostProc")
@Scope("singleton")
public class ExceptionHandlerExceptionResolverBeanPostProcessor extends AbstractHandlerComponentBeanPostProcessor<ExceptionHandlerExceptionResolver> {
    public ExceptionHandlerExceptionResolverBeanPostProcessor() {
        super(ExceptionHandlerExceptionResolver.class);
    }

    @Override
    protected List<HttpMessageConverter<?>> buildMessageConverters(ExceptionHandlerExceptionResolver bean) {
        List<HttpMessageConverter<?>> msgConvs = new ArrayList<>();

        for (HttpMessageConverter<?> msgConv : ToolBeanFactoryUtils.getBeansOfType(this.appContext, HttpMessageConverter.class)) {
            msgConvs.add(msgConv);
        }

        return ToolListUtils.addAllFirst(bean.getMessageConverters(), msgConvs);
    }

    @Override
    protected void setMessageConverters(ExceptionHandlerExceptionResolver bean, List<HttpMessageConverter<?>> msgConvs) {
        bean.setMessageConverters(msgConvs);
    }

    @Override
    protected List<HandlerMethodArgumentResolver> buildHandlerMethodArgumentResolvers(ExceptionHandlerExceptionResolver bean) {
        return ToolCollectionUtils.addAll(new ArrayList<>(bean.getArgumentResolvers().getResolvers()),
            ToolBeanFactoryUtils.getBeansOfType(this.appContext, HandlerMethodArgumentResolver.class));
    }

    @Override
    protected void setHandlerMethodArgumentResolvers(ExceptionHandlerExceptionResolver bean, List<HandlerMethodArgumentResolver> handlerMethodArgResolvers) {
        bean.setArgumentResolvers(handlerMethodArgResolvers);
    }

    @Override
    protected List<HandlerMethodReturnValueHandler> buildHandlerMethodReturnValueHandlers(ExceptionHandlerExceptionResolver bean) {
        return ToolCollectionUtils.addAll(new ArrayList<>(bean.getReturnValueHandlers().getHandlers()),
            ToolBeanFactoryUtils.getBeansOfType(this.appContext, HandlerMethodReturnValueHandler.class));
    }

    @Override
    protected void setHandlerMethodReturnValueHandlers(ExceptionHandlerExceptionResolver bean,
        List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers) {
        bean.setReturnValueHandlers(handlerMethodReturnValueHandlers);
    }
}
