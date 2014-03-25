package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("versionController")
@DisplayController
public class VersionController extends AbstractToolController {
    @RequestMapping(value = { "/version" }, method = { RequestMethod.GET })
    @RequestView("version")
    public ModelAndView displayVersion() {
        return new ModelAndView();
    }
}
