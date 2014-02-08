package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminController")
@DisplayController
public class AdminController extends AbstractToolController {
    @RequestMapping(value = { "/admin/login" }, method = { RequestMethod.GET })
    @RequestView("admin-login")
    public ModelAndView displayAdminLogin() {
        return new ModelAndView();
    }

    @RequestMapping(value = { "/admin" }, method = { RequestMethod.GET })
    @RequestView("admin")
    public ModelAndView displayAdmin() {
        return new ModelAndView();
    }
}
