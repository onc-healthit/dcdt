package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("discoveryController")
@DisplayController
public class DiscoveryController extends AbstractToolController {
    @RequestMapping(value = { "/discovery" }, method = { RequestMethod.GET })
    @RequestView("discovery")
    public ModelAndView displayDiscovery() {
        return new ModelAndView();
    }

    @ModelAttribute("discoveryTestcases")
    private List<DiscoveryTestcase> getDiscoveryTestcasesModelAttribute() {
        return ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class);
    }
}
