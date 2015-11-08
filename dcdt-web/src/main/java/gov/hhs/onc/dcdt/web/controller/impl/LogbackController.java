package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.servlet.impl.LogbackStatusServlet;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@ConditionalOnWebApplication
@Controller("logbackController")
@DisplayController
public class LogbackController extends AbstractToolController {
    private final static String SERVLET_REG_BEAN_BEAN_NAME = "regBeanServletLogbackStatus";

    @Resource(name = "authReqCacheSession")
    private RequestCache authReqCache;

    private String servletName;

    @Nullable
    @RequestMapping(value = { "/logback/status" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView forwardToStatus(HttpServletRequest servletReq, HttpServletResponse servletResp) throws ToolWebException {
        servletReq.setAttribute(LogbackStatusServlet.REQ_ATTR_NAME_AUTH_REQ_CACHE, this.authReqCache);

        try {
            servletReq.getServletContext().getNamedDispatcher(this.servletName).forward(servletReq, servletResp);
        } catch (IOException | ServletException e) {
            throw new ToolWebException(String.format("Unable to forward Logback status servlet (name=%s) request.", this.servletName), e);
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.servletName = this.appContext.getBean(SERVLET_REG_BEAN_BEAN_NAME, ServletRegistrationBean.class).getServletName();
    }
}
