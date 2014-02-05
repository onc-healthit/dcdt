package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("homeController")
@DisplayController
@Scope("singleton")
public class HomeController extends AbstractToolController {
    @RequestMapping(value = { "/", "/home" }, method = { RequestMethod.GET })
    @RequestView("home")
    public ModelAndView displayHome() {
        return new ModelAndView();
    }
}
