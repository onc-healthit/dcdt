package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanPostProcessor;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractHandlerComponentBeanPostProcessor<T> extends AbstractToolBeanPostProcessor<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractHandlerComponentBeanPostProcessor.class);

    protected AbstractHandlerComponentBeanPostProcessor(Class<T> handlerCompClass) {
        super(handlerCompClass, false, true);
    }

    @Override
    protected T postProcessAfterInitializationInternal(T bean, String beanName) throws Exception {
        List<HttpMessageConverter<?>> msgConvs = this.buildMessageConverters(bean);

        LOGGER.debug(String.format("Built %d handler component (class=%s) message converter(s).", msgConvs.size(), ToolClassUtils.getName(this)));

        this.setMessageConverters(bean, msgConvs);

        List<HandlerMethodArgumentResolver> handlerMethodArgResolvers = this.buildHandlerMethodArgumentResolvers(bean);

        LOGGER.debug(String.format("Built %d handler component (class=%s) method argument resolver(s).", handlerMethodArgResolvers.size(),
            ToolClassUtils.getName(this)));

        this.setHandlerMethodArgumentResolvers(bean,
            ToolArrayUtils.asList((HandlerMethodArgumentResolver) new OrderedHandlerMethodArgumentResolverComposite(handlerMethodArgResolvers)));

        List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = this.buildHandlerMethodReturnValueHandlers(bean);

        LOGGER.debug(String.format("Built %d handler component (class=%s) method return value handler(s).", handlerMethodReturnValueHandlers.size(),
            ToolClassUtils.getName(this)));

        this.setHandlerMethodReturnValueHandlers(bean,
            ToolArrayUtils.asList((HandlerMethodReturnValueHandler) new OrderedHandlerMethodReturnValueHandlerComposite(handlerMethodReturnValueHandlers)));

        return super.postProcessAfterInitializationInternal(bean, beanName);
    }

    protected abstract List<HttpMessageConverter<?>> buildMessageConverters(T bean);

    protected abstract void setMessageConverters(T bean, List<HttpMessageConverter<?>> msgConvs);

    protected abstract List<HandlerMethodArgumentResolver> buildHandlerMethodArgumentResolvers(T bean);

    protected abstract void setHandlerMethodArgumentResolvers(T bean, List<HandlerMethodArgumentResolver> handlerMethodArgResolvers);

    protected abstract List<HandlerMethodReturnValueHandler> buildHandlerMethodReturnValueHandlers(T bean);

    protected abstract void setHandlerMethodReturnValueHandlers(T bean, List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers);
}
