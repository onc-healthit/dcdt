package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
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

@Controller("hostingController")
@Scope("singleton")
public class HostingController extends AbstractToolController {
    private final static String MODEL_ATTR_KEY_HOSTING_TESTCASES = "hostingTestcases";

    @RequestMapping(value = { "/hosting" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("hosting") })
    public ModelAndView displayHosting(ModelMap modelMap) throws ToolWebException {
        modelMap.addAttribute(MODEL_ATTR_KEY_HOSTING_TESTCASES, ToolBeanFactoryUtils.getBeansOfType(this.appContext.getBeanFactory(), HostingTestcase.class));

        return this.display(modelMap);
    }
}
