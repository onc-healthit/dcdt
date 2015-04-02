package gov.hhs.onc.dcdt.web.handler.impl;

import javax.annotation.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class OrderedHandlerMethodReturnValueHandlerComposite extends AbstractOrderedHandlerComponentComposite<HandlerMethodReturnValueHandler> implements
    HandlerMethodReturnValueHandler {
    private static class OrderedHandlerMethodReturnValueHandlerWrapper extends AbstractOrderedHandlerComponentWrapper<HandlerMethodReturnValueHandler>
        implements HandlerMethodReturnValueHandler {
        public OrderedHandlerMethodReturnValueHandlerWrapper(HandlerMethodReturnValueHandler handlerComp) {
            super(handlerComp);
        }

        @Override
        public void
            handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
                throws Exception {
            this.handlerComp.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }

        @Override
        public boolean supportsReturnType(MethodParameter returnType) {
            return this.handlerComp.supportsReturnType(returnType);
        }
    }

    public OrderedHandlerMethodReturnValueHandlerComposite() {
        super();
    }

    public OrderedHandlerMethodReturnValueHandlerComposite(@Nullable Iterable<HandlerMethodReturnValueHandler> handlerComps) {
        super(handlerComps);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
        throws Exception {
        HandlerMethodReturnValueHandler handlerMethodReturnValueHandler = this.findHandlerMethodReturnValueHandler(returnType);

        if (handlerMethodReturnValueHandler != null) {
            handlerMethodReturnValueHandler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return this.findHandlerMethodReturnValueHandler(returnType) != null;
    }

    @Override
    protected HandlerMethodReturnValueHandler wrapUnorderedHandlerComponent(HandlerMethodReturnValueHandler handlerComp) {
        return new OrderedHandlerMethodReturnValueHandlerWrapper(handlerComp);
    }

    @Nullable
    private HandlerMethodReturnValueHandler findHandlerMethodReturnValueHandler(MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handlerComp : this.handlerComps) {
            if (handlerComp.supportsReturnType(returnType)) {
                return handlerComp;
            }
        }

        return null;
    }
}
