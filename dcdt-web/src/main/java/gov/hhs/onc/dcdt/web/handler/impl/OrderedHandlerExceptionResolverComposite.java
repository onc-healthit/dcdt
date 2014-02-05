package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.handler.AddModelAttribute;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class OrderedHandlerExceptionResolverComposite extends AbstractOrderedHandlerComponentComposite<HandlerExceptionResolver> implements
    HandlerExceptionResolver {
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

    public OrderedHandlerExceptionResolverComposite() {
        super();
    }

    public OrderedHandlerExceptionResolverComposite(@Nullable Iterable<HandlerExceptionResolver> handlerComps) {
        super(handlerComps);
    }

    @Nullable
    @Override
    @SuppressWarnings({ "ConstantConditions" })
    public ModelAndView
        resolveException(HttpServletRequest servletReq, HttpServletResponse servletResp, @Nullable Object exceptionHandler, Exception exception) {
        ModelAndView mav = null;

        for (HandlerExceptionResolver handlerComp : this.handlerComps) {
            if ((mav = handlerComp.resolveException(servletReq, servletResp, exceptionHandler, exception)) != null) {
                break;
            }
        }

        if ((mav != null) && mav.isReference() && ToolClassUtils.isAssignable(ToolClassUtils.getClass(exceptionHandler), HandlerMethod.class)) {
            ModelMap modelMap = mav.getModelMap();
            HandlerMethod handlerMethod = (HandlerMethod) exceptionHandler;
            Class<? extends Exception> exceptionClass = exception.getClass();
            AddModelAttribute addModelAttrAnno;

            for (MethodParameter methodParam : handlerMethod.getMethodParameters()) {
                if (methodParam.getParameterType().equals(exceptionClass)
                    && methodParam.hasParameterAnnotation(AddModelAttribute.class)
                    && (!modelMap.containsAttribute((addModelAttrAnno = methodParam.getParameterAnnotation(AddModelAttribute.class)).value()) || addModelAttrAnno
                        .override())) {
                    modelMap.addAttribute(addModelAttrAnno.value(), exception);
                }
            }
        }

        return mav;
    }

    @Override
    protected HandlerExceptionResolver wrapUnorderedHandlerComponent(HandlerExceptionResolver handlerComp) {
        return new OrderedHandlerExceptionResolverWrapper(handlerComp);
    }
}
