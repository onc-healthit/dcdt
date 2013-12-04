package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolMethodUtils;
import gov.hhs.onc.dcdt.version.ToolVersion;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.config.GoogleAnalyticsConfig;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import gov.hhs.onc.dcdt.web.controller.ToolController;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public abstract class AbstractToolController extends AbstractToolBean implements ToolController {
    protected final static String MODEL_ATTR_KEY_ERROR = "error";
    protected final static String MODEL_ATTR_KEY_GOOGLE_ANALYTICS = "googleAnalytics";
    protected final static String MODEL_ATTR_KEY_USER = "user";
    protected final static String MODEL_ATTR_KEY_VERSION = "version";

    protected final static Class<?> USER_CLASS = User.class;

    @Autowired
    protected ToolVersion version;

    @Autowired
    protected GoogleAnalyticsConfig googleAnalyticsConfig;

    @ExceptionHandler({ Throwable.class })
    @RequestViews({ @RequestView(value = "/error", forward = true),
        @RequestView(value = "/error-json", contentTypes = { MediaType.APPLICATION_JSON_VALUE }, forward = true) })
    public ModelAndView displayError(Throwable error, HttpServletRequest httpServletReq) throws ToolWebException {
        httpServletReq.setAttribute(MODEL_ATTR_KEY_ERROR, error);
        
        return this.display(null, httpServletReq.getContentType());
    }

    protected ModelAndView display() throws ToolWebException {
        return this.display(null, null, ToolMethodUtils.getCalls());
    }

    protected ModelAndView display(@Nullable ModelMap modelMap) throws ToolWebException {
        return this.display(modelMap, null, ToolMethodUtils.getCalls());
    }

    protected ModelAndView display(@Nullable ModelMap modelMap, @Nullable String reqContentType) throws ToolWebException {
        return this.display(modelMap, reqContentType, ToolMethodUtils.getCalls());
    }

    protected ModelAndView display(@Nullable ModelMap modelMap, @Nullable String reqContentType, List<Pair<Class<?>, Method>> displayCalls)
        throws ToolWebException {
        return this.displayView(this.findRequestView(displayCalls, reqContentType), this.displayMapAttributes(modelMap));
    }

    protected ModelAndView displayView(String reqViewName, @Nullable ModelMap modelMap) throws ToolWebException {
        return new ModelAndView(reqViewName, this.displayMapAttributes(modelMap));
    }

    protected ModelMap displayMapAttributes(@Nullable ModelMap modelMap) {
        if (modelMap == null) {
            return null;
        }

        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (ToolClassUtils.isAssignable(USER_CLASS, ToolClassUtils.getClass(userObj))) {
            modelMap.addAttribute(MODEL_ATTR_KEY_USER, userObj);
        }

        modelMap.addAttribute(MODEL_ATTR_KEY_VERSION, this.version);
        modelMap.addAttribute(MODEL_ATTR_KEY_GOOGLE_ANALYTICS, this.googleAnalyticsConfig);

        return modelMap;
    }

    protected String findRequestView(List<Pair<Class<?>, Method>> displayCalls) throws ToolWebException {
        return this.findRequestView(displayCalls, null);
    }

    protected String findRequestView(@Size(min = 1) List<Pair<Class<?>, Method>> displayCalls, @Nullable String reqContentType) throws ToolWebException {
        Pair<Class<?>, Method> displayCaller = ToolListUtils.getFirst(displayCalls);
        RequestViews reqViewsAnno = ToolAnnotationUtils.findAnnotation(RequestViews.class, displayCalls);

        if (reqViewsAnno == null) {
            throw new ToolWebException(String.format("Unable to find request views annotation on controller (class=%s) request method (name=%s).",
                ToolClassUtils.getName(displayCaller.getLeft()), ToolMethodUtils.getName(displayCaller.getRight())));
        }

        RequestView[] reqViewAnnos = reqViewsAnno.value();

        if (ArrayUtils.isEmpty(reqViewAnnos)) {
            throw new ToolWebException(String.format(
                "Request views annotation on controller (class=%s) request method (name=%s) must contain at least one request view.",
                ToolClassUtils.getName(displayCaller.getLeft()), ToolMethodUtils.getName(displayCaller.getRight())));
        }

        String reqViewName;
        boolean reqContentTypeAny = StringUtils.isBlank(reqContentType);
        String[] reqViewContentTypes;

        for (RequestView reqViewAnno : reqViewAnnos) {
            if (StringUtils.isBlank(reqViewName = reqViewAnno.value())) {
                throw new ToolWebException(String.format("Request view annotation on controller (class=%s) request method (name=%s) must not be blank.",
                    ToolClassUtils.getName(displayCaller.getLeft()), ToolMethodUtils.getName(displayCaller.getRight())));
            }

            if (reqViewAnno.forward()) {
                reqViewName = UrlBasedViewResolver.FORWARD_URL_PREFIX + reqViewName;
            } else if (reqViewAnno.redirect()) {
                reqViewName = UrlBasedViewResolver.REDIRECT_URL_PREFIX + reqViewName;
            }

            if (ArrayUtils.isEmpty(reqViewContentTypes = reqViewAnno.contentTypes())) {
                if (reqContentTypeAny) {
                    return reqViewName;
                }
            } else {
                for (String reqViewContentType : reqViewContentTypes) {
                    if ((reqContentTypeAny && StringUtils.isBlank(reqViewContentType)) || StringUtils.startsWith(reqContentType, reqViewContentType)) {
                        return reqViewName;
                    }
                }
            }
        }

        throw new ToolWebException(String.format(
            "No matching (contentType=%s) request view annotation found on controller (class=%s) request method (name=%s).", reqContentType,
            ToolClassUtils.getName(displayCaller.getLeft()), ToolMethodUtils.getName(displayCaller.getRight())));
    }
}
