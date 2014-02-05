package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("hostingJsonController")
@JsonController
@Scope("singleton")
public class HostingJsonController extends AbstractToolController {
    @RequestMapping(value = { "/hosting/process" }, method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseJsonWrapper<HostingTestcaseResult> processHostingTestcase(
        @RequestBody @Validated RequestJsonWrapper<HostingTestcaseSubmission> reqJsonWrapper, BindingResult bindingResult) {
        ResponseJsonWrapperBuilder<HostingTestcaseResult> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            HostingTestcaseSubmission reqHostingTestcaseSubmission = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqHostingTestcaseSubmission != null) {
                // TODO: implement
                respJsonWrapperBuilder.addItems(new HostingTestcaseResultImpl());
            }
        }

        return respJsonWrapperBuilder.build();
    }
}
