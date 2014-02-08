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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component("reqMappingHandlerAdapterBeanPostProc")
@Scope("singleton")
public class RequestMappingHandlerAdapterBeanPostProcessor extends AbstractHandlerComponentBeanPostProcessor<RequestMappingHandlerAdapter> {
    public RequestMappingHandlerAdapterBeanPostProcessor() {
        super(RequestMappingHandlerAdapter.class);
    }

    @Override
    protected List<HttpMessageConverter<?>> buildMessageConverters(RequestMappingHandlerAdapter bean) {
        List<HttpMessageConverter<?>> msgConvs = new ArrayList<>();

        for (HttpMessageConverter<?> msgConv : ToolBeanFactoryUtils.getBeansOfType(this.appContext, HttpMessageConverter.class)) {
            msgConvs.add(msgConv);
        }

        return ToolListUtils.addAllFirst(bean.getMessageConverters(), msgConvs);
    }

    @Override
    protected void setMessageConverters(RequestMappingHandlerAdapter bean, List<HttpMessageConverter<?>> msgConvs) {
        bean.setMessageConverters(msgConvs);
    }

    @Override
    protected List<HandlerMethodArgumentResolver> buildHandlerMethodArgumentResolvers(RequestMappingHandlerAdapter bean) {
        return ToolCollectionUtils.addAll(new ArrayList<>(bean.getArgumentResolvers()),
            ToolBeanFactoryUtils.getBeansOfType(this.appContext, HandlerMethodArgumentResolver.class));
    }

    @Override
    protected void setHandlerMethodArgumentResolvers(RequestMappingHandlerAdapter bean, List<HandlerMethodArgumentResolver> handlerMethodArgResolvers) {
        bean.setArgumentResolvers(handlerMethodArgResolvers);
    }

    @Override
    protected List<HandlerMethodReturnValueHandler> buildHandlerMethodReturnValueHandlers(RequestMappingHandlerAdapter bean) {
        return ToolCollectionUtils.addAll(new ArrayList<>(bean.getReturnValueHandlers()),
            ToolBeanFactoryUtils.getBeansOfType(this.appContext, HandlerMethodReturnValueHandler.class));
    }

    @Override
    protected void
        setHandlerMethodReturnValueHandlers(RequestMappingHandlerAdapter bean, List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers) {
        bean.setReturnValueHandlers(handlerMethodReturnValueHandlers);
    }
}
