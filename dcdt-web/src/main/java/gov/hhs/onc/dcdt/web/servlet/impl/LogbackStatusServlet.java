package gov.hhs.onc.dcdt.web.servlet.impl;

import ch.qos.logback.classic.ViewStatusMessagesServlet;
import gov.hhs.onc.dcdt.web.security.WebSecurityRoles;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.RequestCache;

public class LogbackStatusServlet extends ViewStatusMessagesServlet {
    public final static String REQ_ATTR_NAME_AUTH_REQ_CACHE = "authReqCache";
    
    private final static long serialVersionUID = 0L;

    @Override
    protected void service(HttpServletRequest servletReq, HttpServletResponse servletResp) throws IOException, ServletException {
        if (!servletReq.isUserInRole(WebSecurityRoles.ADMIN)) {
            ((RequestCache) servletReq.getAttribute(REQ_ATTR_NAME_AUTH_REQ_CACHE)).saveRequest(servletReq, servletResp);
            
            servletReq.authenticate(servletResp);
        }
        
        super.service(servletReq, servletResp);
    }
}
