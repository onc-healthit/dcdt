package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import gov.hhs.onc.dcdt.web.json.ErrorJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ErrorsJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseStatus;
import gov.hhs.onc.dcdt.web.json.impl.ErrorJsonWrapperImpl;
import gov.hhs.onc.dcdt.web.json.impl.ErrorsJsonWrapperImpl;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller("errorController")
@Scope("singleton")
public class ErrorController extends AbstractToolController {
    @RequestMapping(value = { "/errors-json" }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseJsonWrapper<ToolBean> displayErrorsJson(HttpServletRequest httpServletReq) throws ToolWebException {
        ResponseJsonWrapper<ToolBean> resp = new ResponseJsonWrapperImpl<>();

        ErrorsJsonWrapper errors = new ErrorsJsonWrapperImpl();
        errors.setGlobalErrors(ToolArrayUtils.asList((ErrorJsonWrapper) new ErrorJsonWrapperImpl((Throwable) httpServletReq
            .getAttribute(SERVLET_REQ_ATTR_KEY_ERROR))));
        resp.setErrors(errors);
        resp.setStatus(ResponseStatus.ERROR);

        return resp;
    }

    @RequestMapping({ "/errors" })
    @RequestViews({ @RequestView("error") })
    public ModelAndView displayError() throws ToolWebException {
        return this.display();
    }
}
