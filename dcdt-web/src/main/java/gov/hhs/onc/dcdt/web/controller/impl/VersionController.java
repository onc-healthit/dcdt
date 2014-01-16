package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("versionController")
@Scope("singleton")
public class VersionController extends AbstractToolController {
    @RequestMapping(value = { "/version" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("version") })
    public ModelAndView displayVersion(ModelMap modelMap) throws ToolWebException {
        return this.display(modelMap);
    }
}
