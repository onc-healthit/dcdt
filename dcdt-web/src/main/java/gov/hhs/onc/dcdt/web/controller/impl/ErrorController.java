package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ErrorJsonWrapperImpl;
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
    @RequestMapping(value = { "/error-json" }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseJsonWrapper<ToolBean> displayErrorJson(HttpServletRequest httpServletReq) throws ToolWebException {
        ResponseJsonWrapper<ToolBean> resp = new ResponseJsonWrapperImpl<>();
        resp.getErrors().add(new ErrorJsonWrapperImpl((Throwable) httpServletReq.getAttribute(MODEL_ATTR_KEY_ERROR)));
        
        return resp;
    }

    @RequestMapping(value = { "/error" }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ModelAndView displayError() throws ToolWebException {
        return this.display();
    }
}
