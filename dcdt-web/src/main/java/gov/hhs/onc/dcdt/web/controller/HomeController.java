package gov.hhs.onc.dcdt.web.controller;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("toolHomeController")
@Scope("singleton")
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayIndex(ModelMap modelMap) {
        return new ModelAndView("home", modelMap);
    }
}
