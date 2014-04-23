package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.handler.AddModelAttribute;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

public class OrderedHandlerExceptionResolverComposite extends AbstractOrderedHandlerComponentComposite<HandlerExceptionResolver> implements
    ApplicationContextAware, HandlerExceptionResolver {
    private static class OrderedHandlerExceptionResolverWrapper extends AbstractOrderedHandlerComponentWrapper<HandlerExceptionResolver> implements
        HandlerExceptionResolver {
        public OrderedHandlerExceptionResolverWrapper(HandlerExceptionResolver handlerComp) {
            super(handlerComp);
        }

        @Nullable
        @Override
        public ModelAndView resolveException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler,
            Exception exception) {
            return this.handlerComp.resolveException(servletReq, servletResp, exceptionHandler, exception);
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderedHandlerExceptionResolverComposite.class);

    private AbstractApplicationContext appContext;

    public OrderedHandlerExceptionResolverComposite() {
        super();
    }

    public OrderedHandlerExceptionResolverComposite(@Nullable Iterable<HandlerExceptionResolver> handlerComps) {
        super(handlerComps);
    }

    @Nullable
    @Override
    @SuppressWarnings({ "ConstantConditions" })
    public ModelAndView resolveException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object handlerObj, Exception exception) {
        HandlerExceptionResolver resolvingHandlerComp = null;
        ModelAndView mav = null;

        for (HandlerExceptionResolver handlerComp : this.handlerComps) {
            if (((mav = (resolvingHandlerComp = handlerComp).resolveException(servletReq, servletResp, handlerObj, exception)) != null) && mav.isReference()) {
                break;
            }
        }

        ServletInvocableHandlerMethod exceptionHandler;

        if ((mav != null)
            && mav.isReference()
            && ToolClassUtils.isAssignable(ToolClassUtils.getClass(handlerObj), HandlerMethod.class)
            && ToolClassUtils.isAssignable(ToolClassUtils.getClass(resolvingHandlerComp), ToolExceptionHandlerExceptionResolver.class)
            && ((exceptionHandler =
                ((ToolExceptionHandlerExceptionResolver) resolvingHandlerComp).getExceptionHandlerMethod(((HandlerMethod) handlerObj), exception)) != null)) {
            Method exceptionHandlerMethod = exceptionHandler.getMethod();
            Class<?> exceptionHandlerClass = exceptionHandlerMethod.getDeclaringClass();
            ModelMap modelMap = mav.getModelMap();
            Class<? extends Exception> exceptionClass = exception.getClass();
            AddModelAttribute addModelAttrAnno;
            String addModelAttrName;

            for (MethodParameter exceptionHandlerMethodParam : exceptionHandler.getMethodParameters()) {
                if (ToolClassUtils.isAssignable(exceptionClass, exceptionHandlerMethodParam.getParameterType())
                    && exceptionHandlerMethodParam.hasParameterAnnotation(AddModelAttribute.class)
                    && (!modelMap.containsAttribute((addModelAttrName =
                        (addModelAttrAnno = exceptionHandlerMethodParam.getParameterAnnotation(AddModelAttribute.class)).value())) || addModelAttrAnno
                        .override())) {
                    modelMap.addAttribute(addModelAttrName, exception);

                    LOGGER.trace(String.format(
                        "Added (class=%s) exception (class=%s) model attribute (name=%s) for exception handler method (class=%s, name=%s).",
                        ToolClassUtils.getName(this), ToolClassUtils.getName(exception), addModelAttrName, ToolClassUtils.getName(exceptionHandlerClass),
                        exceptionHandlerMethod.getName()));
                }
            }

            BeanWrapper exceptionHandlerWrapper = ToolBeanUtils.wrap(ToolBeanFactoryUtils.getBeanOfType(this.appContext, exceptionHandlerClass));
            String modelAttrName;
            Object modelAttrValue;

            for (PropertyDescriptor exceptionHandlerPropDesc : ToolBeanPropertyUtils.describeReadable(exceptionHandlerWrapper)) {
                if (!StringUtils.isBlank((modelAttrName =
                    ToolAnnotationUtils.getValue(ModelAttribute.class, String.class, exceptionHandlerPropDesc.getReadMethod())))) {
                    modelMap.addAttribute(modelAttrName, (modelAttrValue = exceptionHandlerWrapper.getPropertyValue(exceptionHandlerPropDesc.getName())));

                    LOGGER.trace(String.format("Added (class=%s) model attribute (name=%s, valueClass=%s) for exception handler method (class=%s, name=%s).",
                        ToolClassUtils.getName(this), modelAttrName, ToolClassUtils.getName(modelAttrValue), ToolClassUtils.getName(exceptionHandlerClass),
                        exceptionHandlerMethod.getName()));
                }
            }
        }

        return mav;
    }

    @Override
    protected HandlerExceptionResolver wrapUnorderedHandlerComponent(HandlerExceptionResolver handlerComp) {
        return new OrderedHandlerExceptionResolverWrapper(handlerComp);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
