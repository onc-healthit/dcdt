package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.handler.AddModelAttribute;
import javax.annotation.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class OrderedHandlerMethodArgumentResolverComposite extends AbstractOrderedHandlerComponentComposite<HandlerMethodArgumentResolver> implements
    HandlerMethodArgumentResolver {
    private static class OrderedHandlerMethodArgumentResolverWrapper extends AbstractOrderedHandlerComponentWrapper<HandlerMethodArgumentResolver> implements
        HandlerMethodArgumentResolver {
        public OrderedHandlerMethodArgumentResolverWrapper(HandlerMethodArgumentResolver handlerComp) {
            super(handlerComp);
        }

        @Nullable
        @Override
        public Object resolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
            return this.handlerComp.resolveArgument(methodParam, mavContainer, webRequest, binderFactory);
        }

        @Override
        public boolean supportsParameter(MethodParameter methodParam) {
            return this.handlerComp.supportsParameter(methodParam);
        }
    }

    public OrderedHandlerMethodArgumentResolverComposite() {
        super();
    }

    public OrderedHandlerMethodArgumentResolverComposite(@Nullable Iterable<HandlerMethodArgumentResolver> handlerComps) {
        super(handlerComps);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {
        HandlerMethodArgumentResolver handlerMethodArgResolver = this.findHandlerMethodArgumentResolver(methodParam);

        if (handlerMethodArgResolver == null) {
            return null;
        }

        Object argObj = handlerMethodArgResolver.resolveArgument(methodParam, mavContainer, webRequest, binderFactory);

        AddModelAttribute addModelAttrAnno;

        if (methodParam.hasParameterAnnotation(AddModelAttribute.class)
            && ((methodParam.getMethodAnnotation(ExceptionHandler.class) == null) || !ToolClassUtils.isAssignable(methodParam.getParameterType(),
                Exception.class))
            && (!mavContainer.containsAttribute((addModelAttrAnno = methodParam.getParameterAnnotation(AddModelAttribute.class)).value()) || addModelAttrAnno
                .override())) {
            mavContainer.addAttribute(addModelAttrAnno.value(), argObj);
        }

        return argObj;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParam) {
        return this.findHandlerMethodArgumentResolver(methodParam) != null;
    }

    @Override
    protected HandlerMethodArgumentResolver wrapUnorderedHandlerComponent(HandlerMethodArgumentResolver handlerComp) {
        return new OrderedHandlerMethodArgumentResolverWrapper(handlerComp);
    }

    @Nullable
    private HandlerMethodArgumentResolver findHandlerMethodArgumentResolver(MethodParameter methodParam) {
        for (HandlerMethodArgumentResolver handlerComp : this.handlerComps) {
            if (handlerComp.supportsParameter(methodParam)) {
                return handlerComp;
            }
        }

        return null;
    }
}
