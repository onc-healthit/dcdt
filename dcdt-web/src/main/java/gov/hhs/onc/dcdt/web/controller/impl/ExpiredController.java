package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("expiredController")
@DisplayController
public class ExpiredController extends AbstractToolController {
    @RequestMapping(value = { "/expired" }, method = { RequestMethod.GET })
    @RequestView("expired")
    public ModelAndView displayExpired() {
        return new ModelAndView();
    }
}
