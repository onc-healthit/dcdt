package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultGeneratorImpl;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapConnectionResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
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
import javax.annotation.Resource;
import java.util.List;

@Controller("hostingJsonController")
@JsonController
@JsonResponse
public class HostingJsonController extends AbstractToolController {
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    @Resource(name = "certDiscoverySteps")
    private List<ToolTestcaseResultStep> certDiscoverySteps;

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
                HostingTestcase hostingTestcase = reqHostingTestcaseSubmission.getHostingTestcase();
                HostingTestcaseResult hostingTestcaseResult = new HostingTestcaseResultImpl();

                if (hostingTestcase != null && hostingTestcase.hasResult()) {
                    hostingTestcaseResult = hostingTestcase.getResult();
                    hostingTestcaseResult.setResultInfo(new HostingTestcaseResultInfoImpl());
                    HostingTestcaseResultGenerator resultGenerator = new HostingTestcaseResultGeneratorImpl(reqHostingTestcaseSubmission);
                    resultGenerator.generateTestcaseResult(this.certDiscoverySteps);
                    updateResultDisplayMessage(hostingTestcase, resultGenerator.getErrorStepPosition(hostingTestcaseResult));
                }
                respJsonWrapperBuilder.addItems(this.getHostingTestcaseResultJsonDto(hostingTestcaseResult));
            }
        }

        return respJsonWrapperBuilder.build();
    }

    private void updateResultDisplayMessage(HostingTestcase hostingTestcase, int errorStepPosition) {
        HostingTestcaseResult result = hostingTestcase.getResult();
        HostingTestcaseResultInfo resultInfo = result.getResultInfo();
        ToolTestcaseResultStep lastStep = ToolListUtils.getLast(resultInfo.getResults());
        ToolStrBuilder msgStrBuilder = new ToolStrBuilder();

        if (lastStep instanceof ToolTestcaseCertificateResultStep) {
            msgStrBuilder.appendWithDelimiter(getCertMessage(hostingTestcase, (ToolTestcaseCertificateResultStep) lastStep, resultInfo.isSuccessful()),
                ToolStringUtils.DELIM_NEW_LINE);
        } else if (lastStep instanceof ToolTestcaseLdapConnectionResultStep) {
            msgStrBuilder.appendWithDelimiter(getLdapMessage((ToolTestcaseLdapConnectionResultStep) lastStep), ToolStringUtils.DELIM_NEW_LINE);
        }

        if (!resultInfo.isSuccessful()) {
            msgStrBuilder.appendWithDelimiter(getErrorMessage(hostingTestcase, errorStepPosition, lastStep), ToolStringUtils.DELIM_NEW_LINE);
        }
        resultInfo.setMessage(msgStrBuilder.build());
    }

    private String getErrorMessage(HostingTestcase hostingTestcase, int errorStepPosition, ToolTestcaseResultStep lastStep) {
        String errorCode = "dcdt.testcase.result.error.step.msg";
        ToolStrBuilder errorMsgStrBuilder = new ToolStrBuilder();

        HostingTestcaseResult result = hostingTestcase.getResult();
        HostingTestcaseResultInfo resultInfo = result.getResultInfo();
        ToolTestcaseResultStep resultConfigErrorStep = result.getResultConfig().getResults().get(errorStepPosition - 1);
        ToolTestcaseResultStep resultInfoErrorStep = resultInfo.getResults().get(errorStepPosition - 1);

        errorMsgStrBuilder.appendWithDelimiter(
            ToolMessageUtils.getMessage(this.msgSource, errorCode, errorStepPosition, resultConfigErrorStep.getDescription().getText(),
                resultConfigErrorStep.isSuccessful(), resultInfoErrorStep.isSuccessful()), ToolStringUtils.DELIM_NEW_LINE);

        Object[] msgParams = new Object[] { hostingTestcase.getBindingType(), hostingTestcase.getLocationType() };

        if (!hostingTestcase.isNegative() && !(lastStep instanceof ToolTestcaseCertificateResultStep)) {
            errorMsgStrBuilder.appendWithDelimiter(
                ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.MISSING_CERT.getMessage(), msgParams),
                ToolStringUtils.DELIM_NEW_LINE);
        }

        if (resultInfoErrorStep.hasMessage()) {
            errorMsgStrBuilder.appendWithDelimiter(resultInfoErrorStep.getMessage(), ToolStringUtils.DELIM_NEW_LINE);
        }

        return errorMsgStrBuilder.build();
    }

    private String getCertMessage(HostingTestcase hostingTestcase, ToolTestcaseCertificateResultStep certResultStep, boolean successful) {
        ToolTestcaseCertificateResultType certResultType = certResultStep.getCertificateStatus();
        Object[] msgParams = new Object[] { certResultStep.getBindingType(), certResultStep.getLocationType() };
        ToolStrBuilder certStrBuilder = new ToolStrBuilder();

        certStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, certResultType.getMessage(), msgParams), ToolStringUtils.DELIM_NEW_LINE);

        if (certResultStep.hasCertificateInfo() && !successful) {
            if (hostingTestcase.isNegative()) {
                certStrBuilder.appendWithDelimiter(
                    ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.UNEXPECTED_CERT.getMessage(), msgParams),
                    ToolStringUtils.DELIM_NEW_LINE);
            } else {
                certStrBuilder.appendWithDelimiter(
                    ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.INCORRECT_CERT.getMessage(), new Object[] {
                        hostingTestcase.getBindingType(), hostingTestcase.getLocationType() }), ToolStringUtils.DELIM_NEW_LINE);
            }
        }
        return certStrBuilder.build();
    }

    private String getLdapMessage(ToolTestcaseLdapConnectionResultStep ldapResultStep) {
        ToolTestcaseLdapResultType ldapResultType = ldapResultStep.getLdapStatus();
        return ToolMessageUtils.getMessage(this.msgSource, ldapResultType.getMessage());
    }

    private HostingTestcaseResultJsonDto getHostingTestcaseResultJsonDto(HostingTestcaseResult hostingTestcaseResult) throws Exception {
        HostingTestcaseResultJsonDto hostingTestcaseResultJsonDto =
            this.appContext.getBean(ToolBeanFactoryUtils.getBeanNameOfType(this.appContext, HostingTestcaseResultJsonDto.class),
                HostingTestcaseResultJsonDto.class);
        hostingTestcaseResultJsonDto.fromBean(this.convService, hostingTestcaseResult);

        return hostingTestcaseResultJsonDto;
    }
}
