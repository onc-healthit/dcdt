package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("hostingController")
@DisplayController
@Scope("singleton")
public class HostingController extends AbstractToolController {
    @RequestMapping(value = { "/hosting" }, method = { RequestMethod.GET })
    @RequestView("hosting")
    public ModelAndView displayHosting() {
        return new ModelAndView();
    }

    @ModelAttribute("hostingTestcases")
    private List<HostingTestcase> getHostingTestcasesModelAttribute() {
        return ToolBeanFactoryUtils.getBeansOfType(this.appContext, HostingTestcase.class);
    }
}
