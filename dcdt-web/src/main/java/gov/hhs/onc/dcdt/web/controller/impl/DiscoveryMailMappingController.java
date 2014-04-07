package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("discoveryMailMappingController")
@DisplayController
public class DiscoveryMailMappingController extends AbstractToolController {
    @RequestMapping(value = { "/discovery/mail/mapping" }, method = { RequestMethod.GET })
    @RequestView("discovery-mail-mapping")
    public ModelAndView displayDiscoveryMailMapping() {
        return new ModelAndView();
    }
}
