package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.utils.ToolViewUtils;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestViewProcessor.class);

    public RequestViewProcessor() {
        super(Object.class, ModelAndView.class, false, true);
    }

    @Override
    protected void handleReturnValueInternal(@Nullable ModelAndView returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest) throws Exception {
        Method handlerMethod = returnType.getMethod();
        RequestView reqViewAnno = ToolAnnotationUtils.findAnnotation(RequestView.class, handlerMethod);
        String reqViewName;

        if (!mavContainer.isRequestHandled() && (returnValue != null) && (reqViewAnno != null)
            && (((returnValue.getViewName() == null) || reqViewAnno.override()))) {
            mavContainer.setViewName((reqViewName =
                ToolViewUtils.overrideDirective(ObjectUtils.defaultIfNull(reqViewAnno.value(), mavContainer.getViewName()), reqViewAnno.directive())));

            LOGGER.trace(String.format("Set (class=%s) request view name for handler method (class=%s, name=%s): %s", ToolClassUtils.getName(this),
                ToolClassUtils.getName(handlerMethod.getDeclaringClass()), handlerMethod.getName(), reqViewName));
        }
    }
}
