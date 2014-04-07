package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.web.utils.ToolViewUtils;
import gov.hhs.onc.dcdt.web.view.RequestView;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

@Component("reqViewProc")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestViewProcessor extends AbstractToolHandlerMethodProcessor<Object, ModelAndView> {
    public RequestViewProcessor() {
        super(Object.class, ModelAndView.class, false, true);
    }

    @Override
    protected void handleReturnValueInternal(@Nullable ModelAndView returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest) throws Exception {
        RequestView reqViewAnno = ToolAnnotationUtils.findAnnotation(RequestView.class, returnType.getMethod());

        if (!mavContainer.isRequestHandled() && (returnValue != null) && (reqViewAnno != null)
            && (((returnValue.getViewName() == null) || reqViewAnno.override()))) {
            mavContainer.setViewName(ToolViewUtils.overrideDirective(ObjectUtils.defaultIfNull(reqViewAnno.value(), mavContainer.getViewName()),
                reqViewAnno.directive()));
        }
    }
}
