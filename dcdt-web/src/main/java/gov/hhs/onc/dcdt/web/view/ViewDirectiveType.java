package gov.hhs.onc.dcdt.web.view;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public enum ViewDirectiveType {
    NONE(false, StringUtils.EMPTY), FORWARD(true, UrlBasedViewResolver.FORWARD_URL_PREFIX), REDIRECT(true, UrlBasedViewResolver.REDIRECT_URL_PREFIX);

    private final boolean directive;
    private final String viewNamePrefix;

    ViewDirectiveType(boolean directive, String viewNamePrefix) {
        this.directive = directive;
        this.viewNamePrefix = viewNamePrefix;
    }

    public boolean isDirective() {
        return this.directive;
    }

    public boolean isForward() {
        return this == FORWARD;
    }

    public boolean isRedirect() {
        return this == REDIRECT;
    }

    public String getViewNamePrefix() {
        return this.viewNamePrefix;
    }
}
