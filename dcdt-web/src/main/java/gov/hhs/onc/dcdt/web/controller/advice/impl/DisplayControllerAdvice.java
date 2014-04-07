package gov.hhs.onc.dcdt.web.controller.advice.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.version.ToolVersion;
import gov.hhs.onc.dcdt.web.config.GoogleAnalyticsConfig;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.handler.AddModelAttribute;
import gov.hhs.onc.dcdt.web.view.RequestView;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Component("displayControllerAdvice")
@ControllerAdvice(annotations = { DisplayController.class })
public class DisplayControllerAdvice extends AbstractToolControllerAdvice<ModelAndView> {
    @Autowired
    private GoogleAnalyticsConfig googleAnalyticsConfig;

    @Autowired
    private ToolVersion version;

    @ExceptionHandler
    @Nullable
    @Override
    @RequestView("error")
    public ModelAndView handleException(@AddModelAttribute("exception") Exception exception) {
        return new ModelAndView();
    }

    @ModelAttribute("googleAnalyticsConfig")
    private GoogleAnalyticsConfig getGoogleAnalyticsConfigModelAttribute() {
        return this.googleAnalyticsConfig;
    }

    @ModelAttribute("user")
    @Nullable
    private User getUserModelAttribute() {
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ToolClassUtils.isAssignable(ToolClassUtils.getClass(userObj), User.class) ? ((User) userObj) : null;
    }

    @ModelAttribute("version")
    private ToolVersion getVersionModelAttribute() {
        return this.version;
    }
}
