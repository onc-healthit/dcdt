package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("discoveryController")
@Scope("singleton")
public class DiscoveryController extends AbstractToolController {
    private final static String MODEL_ATTR_KEY_DISCOVERY_TESTCASES = "discoveryTestcases";

    @RequestMapping(value = { "/discovery" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("discovery") })
    public ModelAndView displayDiscovery(ModelMap modelMap) throws ToolWebException {
        modelMap.addAttribute(MODEL_ATTR_KEY_DISCOVERY_TESTCASES,
            ToolBeanFactoryUtils.getBeansOfType(this.appContext.getBeanFactory(), DiscoveryTestcase.class));

        return this.display(modelMap);
    }
}
