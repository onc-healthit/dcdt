package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller("hostingController")
@Scope("singleton")
public class HostingController extends AbstractToolController {
    private final static String MODEL_ATTR_KEY_HOSTING_TESTCASES = "hostingTestcases";

    @RequestMapping(value = {"/hosting"}, method = {RequestMethod.GET})
    @RequestViews({@RequestView("hosting")})
    public ModelAndView displayHosting(ModelMap modelMap) throws ToolWebException {
        modelMap.addAttribute(MODEL_ATTR_KEY_HOSTING_TESTCASES, ToolBeanFactoryUtils.getBeansOfType(this.appContext.getBeanFactory(), HostingTestcase.class));

        return this.display(modelMap);
    }

    @RequestMapping(value = {"/hosting/process"}, method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseJsonWrapper<HostingTestcaseResult> processHostingTestcase(@RequestBody @Valid RequestJsonWrapper<HostingTestcaseSubmission> req) {
        //TODO: implement
        ResponseJsonWrapper<HostingTestcaseResult> resp = new ResponseJsonWrapperImpl<>();
        HostingTestcaseSubmission reqHostingTestcaseSubmission = ToolListUtils.getFirst(req.getItems());

        if (reqHostingTestcaseSubmission != null) {
            HostingTestcaseResult hostingTestcaseResult = new HostingTestcaseResultImpl();
            resp.getItems().add(hostingTestcaseResult);
        }
        return resp;
    }
}
