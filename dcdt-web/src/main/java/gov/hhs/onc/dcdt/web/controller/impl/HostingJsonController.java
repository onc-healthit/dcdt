package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.controller.JsonRequest;
import gov.hhs.onc.dcdt.web.controller.JsonResponse;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("hostingJsonController")
@JsonController
@JsonResponse
public class HostingJsonController extends AbstractToolController {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseProcessor hostingTestcaseProc;

    @JsonRequest
    @RequestMapping({ "/hosting/process" })
    public ResponseJsonWrapper<HostingTestcaseResult, HostingTestcaseResultJsonDto> processHostingTestcase(
        @RequestBody @Validated RequestJsonWrapper<HostingTestcaseSubmission, HostingTestcaseSubmissionJsonDto> reqJsonWrapper, BindingResult bindingResult)
        throws Exception {
        ResponseJsonWrapperBuilder<HostingTestcaseResult, HostingTestcaseResultJsonDto> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            HostingTestcaseSubmissionJsonDto reqJsonDto = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqJsonDto != null) {
                HostingTestcaseSubmission hostingTestcaseSubmission = reqJsonDto.toBean(this.convService);

                respJsonWrapperBuilder.addItems(this.buildHostingTestcaseResultJsonDto(this.hostingTestcaseProc.process(ToolBeanFactoryUtils.createBeanOfType(
                    this.appContext, HostingTestcaseSubmission.class, hostingTestcaseSubmission.getTestcase(), hostingTestcaseSubmission.getDirectAddress()))));
            }
        }

        return respJsonWrapperBuilder.build();
    }

    private HostingTestcaseResultJsonDto buildHostingTestcaseResultJsonDto(HostingTestcaseResult hostingTestcaseResult) throws Exception {
        HostingTestcaseResultJsonDto hostingTestcaseResultJsonDto =
            this.appContext.getBean(ToolBeanFactoryUtils.getBeanNameOfType(this.appContext, HostingTestcaseResultJsonDto.class),
                HostingTestcaseResultJsonDto.class);
        hostingTestcaseResultJsonDto.fromBean(this.convService, hostingTestcaseResult);

        return hostingTestcaseResultJsonDto;
    }
}
