package gov.hhs.onc.dcdt.web.controller.impl;

import java.util.List;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;

@Controller("hostingController")
@Scope("singleton")
public class HostingController extends AbstractToolController {
    private final static String MODEL_ATTR_KEY_HOSTING = "hosting";

    @Autowired(required = false)
    private List<HostingTestcase> hostingTestcases;

    @RequestMapping(value = { "/hosting" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("hosting") })
    public ModelAndView displayHosting(ModelMap modelMap) throws ToolWebException {
        processDescriptionText();
        modelMap.addAttribute(MODEL_ATTR_KEY_HOSTING, this.hostingTestcases);
        return this.display(modelMap);
    }

    private void processDescriptionText() {
        for (HostingTestcase hostingTestcase : hostingTestcases) {
            HostingTestcaseDescription hostingTestcaseDescription = hostingTestcase.getHostingTestcaseDescription();
            String description = hostingTestcaseDescription.getDescription();
            hostingTestcaseDescription.setDescription(ToolStringUtils.removeExtraWhitespace(description));
            String instructions = hostingTestcaseDescription.getInstructions();
            hostingTestcaseDescription.setInstructions(ToolStringUtils.removeExtraWhitespace(instructions));
        }
    }
}
