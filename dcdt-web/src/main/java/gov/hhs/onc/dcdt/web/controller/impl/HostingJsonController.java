package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseCertificateStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseLdapConnectionStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapResultType;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.controller.JsonRequest;
import gov.hhs.onc.dcdt.web.controller.JsonResponse;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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
    private HostingTestcaseProcessor testcaseProcessor;

    private final static String CERT_DISCOVERY_STEPS = "certDiscoverySteps";

    @JsonRequest
    @RequestMapping({ "/hosting/process" })
    @SuppressWarnings({ "unchecked" })
    public ResponseJsonWrapper<HostingTestcaseResult, HostingTestcaseResultJsonDto> processHostingTestcase(
        @RequestBody @Validated RequestJsonWrapper<HostingTestcaseSubmission, HostingTestcaseSubmissionJsonDto> reqJsonWrapper, BindingResult bindingResult)
        throws Exception {
        ResponseJsonWrapperBuilder<HostingTestcaseResult, HostingTestcaseResultJsonDto> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            HostingTestcaseSubmissionJsonDto reqHostingTestcaseSubmissionJsonDto = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqHostingTestcaseSubmissionJsonDto != null) {
                HostingTestcaseSubmission reqHostingTestcaseSubmission = reqHostingTestcaseSubmissionJsonDto.toBean(this.convService);
                HostingTestcase hostingTestcase = reqHostingTestcaseSubmission.getTestcase();
                HostingTestcaseResult hostingTestcaseResult = new HostingTestcaseResultImpl();

                if (hostingTestcase != null) {
                    hostingTestcaseResult =
                        this.testcaseProcessor.generateTestcaseResult(reqHostingTestcaseSubmission,
                            (List<ToolTestcaseStep>) this.appContext.getBean(CERT_DISCOVERY_STEPS));
                    updateResultDisplayMessage(hostingTestcase, hostingTestcaseResult,
                        this.testcaseProcessor.getErrorStepPosition(hostingTestcase.getConfig(), hostingTestcaseResult));
                }
                respJsonWrapperBuilder.addItems(this.getHostingTestcaseResultJsonDto(hostingTestcaseResult));
            }
        }

        return respJsonWrapperBuilder.build();
    }

    private void updateResultDisplayMessage(HostingTestcase hostingTestcase, HostingTestcaseResult result, int errorStepPosition) {
        ToolTestcaseStep lastStep = ToolListUtils.getLast(result.getInfoSteps());
        ToolStrBuilder msgStrBuilder = new ToolStrBuilder();

        if (lastStep instanceof ToolTestcaseCertificateStep) {
            msgStrBuilder.appendWithDelimiter(getCertMessage(hostingTestcase, (ToolTestcaseCertificateStep) lastStep, result.isSuccessful()), StringUtils.LF);
        } else if (lastStep instanceof ToolTestcaseLdapConnectionStep) {
            msgStrBuilder.appendWithDelimiter(getLdapMessage((ToolTestcaseLdapConnectionStep) lastStep), StringUtils.LF);
        }

        if (!result.isSuccessful()) {
            msgStrBuilder.appendWithDelimiter(getErrorMessage(hostingTestcase, result, lastStep, errorStepPosition), StringUtils.LF);
        }
        result.setMessage(msgStrBuilder.build());
    }

    private String getErrorMessage(HostingTestcase hostingTestcase, HostingTestcaseResult result, ToolTestcaseStep lastStep, int errorStepPosition) {
        String errorCode = "dcdt.testcase.result.error.step.msg";
        ToolStrBuilder errorMsgStrBuilder = new ToolStrBuilder();

        // noinspection ConstantConditions
        ToolTestcaseStep configErrorStep = hostingTestcase.getConfig().getConfigSteps().get(errorStepPosition - 1);
        // noinspection ConstantConditions
        ToolTestcaseStep infoErrorStep = result.getInfoSteps().get(errorStepPosition - 1);

        // noinspection ConstantConditions
        errorMsgStrBuilder.appendWithDelimiter(
            ToolMessageUtils.getMessage(this.msgSource, errorCode, errorStepPosition, configErrorStep.getDescription().getText(),
                configErrorStep.isSuccessful(), infoErrorStep.isSuccessful()), StringUtils.LF);

        Object[] msgParams = new Object[] { hostingTestcase.getBindingType(), hostingTestcase.getLocationType() };

        if (!hostingTestcase.isNegative() && !(lastStep instanceof ToolTestcaseCertificateStep)) {
            errorMsgStrBuilder.appendWithDelimiter(
                ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.MISSING_CERT.getMessage(), msgParams), StringUtils.LF);
        }

        if (infoErrorStep.hasMessage()) {
            errorMsgStrBuilder.appendWithDelimiter(infoErrorStep.getMessage(), StringUtils.LF);
        }

        return errorMsgStrBuilder.build();
    }

    private String getCertMessage(HostingTestcase hostingTestcase, ToolTestcaseCertificateStep certInfoStep, boolean successful) {
        ToolTestcaseCertificateResultType certResultType = certInfoStep.getCertificateStatus();
        Object[] certStepMsgParams = new Object[] { certInfoStep.getBindingType(), certInfoStep.getLocationType() };
        Object[] testcaseMsgParams = new Object[] { hostingTestcase.getBindingType(), hostingTestcase.getLocationType() };
        ToolStrBuilder certStrBuilder = new ToolStrBuilder();

        if (certResultType == ToolTestcaseCertificateResultType.NO_CERT) {
            certStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, certResultType.getMessage(), testcaseMsgParams), StringUtils.LF);
        } else {
            certStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, certResultType.getMessage(), certStepMsgParams), StringUtils.LF);
        }

        if (certInfoStep.hasCertificateInfo() && !successful) {
            if (hostingTestcase.isNegative()) {
                certStrBuilder.appendWithDelimiter(
                    ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.UNEXPECTED_CERT.getMessage(), certStepMsgParams),
                    StringUtils.LF);
            } else {
                certStrBuilder.appendWithDelimiter(
                    ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.INCORRECT_CERT.getMessage(), testcaseMsgParams),
                    StringUtils.LF);
            }
        }
        return certStrBuilder.build();
    }

    private String getLdapMessage(ToolTestcaseLdapConnectionStep ldapInfoStep) {
        ToolTestcaseLdapResultType ldapResultType = ldapInfoStep.getLdapStatus();
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
