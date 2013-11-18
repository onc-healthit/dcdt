package gov.hhs.onc.dcdt.web.controller.impl;


import gov.hhs.onc.dcdt.web.RequestView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("homeController")
@Scope("singleton")
public class HomeController extends AbstractToolController {
    @RequestMapping(value = { "/", "/home" }, method = { RequestMethod.GET })
    @RequestView("home")
    public ModelAndView displayHome(ModelMap modelMap) {
        return this.display(modelMap);
    }
}
