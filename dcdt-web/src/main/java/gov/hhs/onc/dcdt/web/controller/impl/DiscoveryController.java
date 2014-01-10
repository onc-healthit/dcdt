package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@Controller("discoveryController")
@Scope("singleton")
public class DiscoveryController extends AbstractToolController {
    private final static String MODEL_ATTR_KEY_DISCOVERY = "discovery";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<DiscoveryTestcase> discoveryTestcases;

    @RequestMapping(value = { "/discovery" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("discovery") })
    public ModelAndView displayDiscovery(ModelMap modelMap) throws ToolWebException {
        processDescriptionText();
        modelMap.addAttribute(MODEL_ATTR_KEY_DISCOVERY, this.discoveryTestcases);
        return this.display(modelMap);
    }

    private void processDescriptionText() {
        for (DiscoveryTestcase discoveryTestcase : discoveryTestcases) {
            DiscoveryTestcaseDescription discoveryTestcaseDescription = discoveryTestcase.getDiscoveryTestcaseDescription();
            String description = discoveryTestcaseDescription.getDescription();
            discoveryTestcaseDescription.setDescription(ToolStringUtils.removeExtraWhitespace(description));
            String instructions = discoveryTestcaseDescription.getInstructions();
            discoveryTestcaseDescription.setInstructions(ToolStringUtils.removeExtraWhitespace(instructions));
            String targetCertificate = discoveryTestcaseDescription.getTargetCertificate();
            discoveryTestcaseDescription.setTargetCertificate(ToolStringUtils.removeExtraWhitespace(targetCertificate));
            List<String> processedBackgroundCerts = new ArrayList<>();
            for (String backgroundCertificate : discoveryTestcaseDescription.getBackgroundCertificates()) {
                processedBackgroundCerts.add(ToolStringUtils.removeExtraWhitespace(backgroundCertificate));
            }
            discoveryTestcaseDescription.setBackgroundCertificates(processedBackgroundCerts);
        }
    }
}
