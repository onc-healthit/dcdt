package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("homeController")
@Scope("singleton")
public class HomeController extends AbstractToolController {
    @RequestMapping(value = { "/" }, method = { RequestMethod.GET })
    @RequestViews(value = { @RequestView(value = "home", forward = true) })
    public ModelAndView displayHomeDefault(ModelMap modelMap) throws ToolWebException {
        return this.displayHome(modelMap);
    }

    @RequestMapping(value = { "/home" }, method = { RequestMethod.GET })
    @RequestViews(value = { @RequestView("home") })
    public ModelAndView displayHome(ModelMap modelMap) throws ToolWebException {
        return this.display(modelMap);
    }
}
