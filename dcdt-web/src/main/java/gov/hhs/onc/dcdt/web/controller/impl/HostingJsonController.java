package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.controller.JsonRequest;
import gov.hhs.onc.dcdt.web.controller.JsonResponse;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("hostingJsonController")
@JsonController
@JsonResponse
public class HostingJsonController extends AbstractToolController {
    @JsonRequest
    @RequestMapping({ "/hosting/process" })
    public ResponseJsonWrapper<HostingTestcaseResult, HostingTestcaseResultJsonDto> processHostingTestcase(
        @RequestBody @Validated RequestJsonWrapper<HostingTestcaseSubmission, HostingTestcaseSubmissionJsonDto> reqJsonWrapper, BindingResult bindingResult)
        throws Exception {
        ResponseJsonWrapperBuilder<HostingTestcaseResult, HostingTestcaseResultJsonDto> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            HostingTestcaseSubmissionJsonDto reqHostingTestcaseSubmissionJsonDto = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqHostingTestcaseSubmissionJsonDto != null) {
                HostingTestcaseSubmission reqHostingTestcaseSubmission = reqHostingTestcaseSubmissionJsonDto.toBean(this.convService);

                // TODO: implement
                respJsonWrapperBuilder.addItems(this.getHostingTestcaseResultJsonDto(new HostingTestcaseResultImpl()));
            }
        }

        return respJsonWrapperBuilder.build();
    }

    private HostingTestcaseResultJsonDto getHostingTestcaseResultJsonDto(HostingTestcaseResult hostingTestcaseResult) throws Exception {
        HostingTestcaseResultJsonDto hostingTestcaseResultJsonDto =
            this.appContext.getBean(ToolBeanFactoryUtils.getBeanNameOfType(this.appContext, HostingTestcaseResultJsonDto.class),
                HostingTestcaseResultJsonDto.class);
        hostingTestcaseResultJsonDto.fromBean(this.convService, hostingTestcaseResult);

        return hostingTestcaseResultJsonDto;
    }
}
