package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseSubmissionJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultJsonDto;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultImpl;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapConnectionResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
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
import javax.annotation.Resource;
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
    @Resource(name = "certDiscoverySteps")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<ToolTestcaseResultStep> certDiscoverySteps;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseResultGenerator resultGenerator;

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
                    // noinspection ConstantConditions
                    hostingTestcaseResult.setResultInfo(new HostingTestcaseResultInfoImpl());
                    this.resultGenerator.setSubmission(reqHostingTestcaseSubmission);
                    this.resultGenerator.generateTestcaseResult(this.certDiscoverySteps);
                    updateResultDisplayMessage(hostingTestcase, this.resultGenerator.getErrorStepPosition(hostingTestcaseResult));
                }
                respJsonWrapperBuilder.addItems(this.getHostingTestcaseResultJsonDto(hostingTestcaseResult));
            }
        }

        return respJsonWrapperBuilder.build();
    }

    @SuppressWarnings({ "ConstantConditions" })
    private void updateResultDisplayMessage(HostingTestcase hostingTestcase, int errorStepPosition) {
        HostingTestcaseResult result = hostingTestcase.getResult();
        HostingTestcaseResultInfo resultInfo = result.getResultInfo();
        ToolTestcaseResultStep lastStep = ToolListUtils.getLast(resultInfo.getResults());
        ToolStrBuilder msgStrBuilder = new ToolStrBuilder();

        if (lastStep instanceof ToolTestcaseCertificateResultStep) {
            msgStrBuilder.appendWithDelimiter(getCertMessage(hostingTestcase, (ToolTestcaseCertificateResultStep) lastStep, resultInfo.isSuccessful()),
                StringUtils.LF);
        } else if (lastStep instanceof ToolTestcaseLdapConnectionResultStep) {
            msgStrBuilder.appendWithDelimiter(getLdapMessage((ToolTestcaseLdapConnectionResultStep) lastStep), StringUtils.LF);
        }

        if (!resultInfo.isSuccessful()) {
            msgStrBuilder.appendWithDelimiter(getErrorMessage(hostingTestcase, errorStepPosition, lastStep), StringUtils.LF);
        }
        resultInfo.setMessage(msgStrBuilder.build());
    }

    @SuppressWarnings({ "ConstantConditions" })
    private String getErrorMessage(HostingTestcase hostingTestcase, int errorStepPosition, ToolTestcaseResultStep lastStep) {
        String errorCode = "dcdt.testcase.result.error.step.msg";
        ToolStrBuilder errorMsgStrBuilder = new ToolStrBuilder();

        HostingTestcaseResult result = hostingTestcase.getResult();
        HostingTestcaseResultInfo resultInfo = result.getResultInfo();
        ToolTestcaseResultStep resultConfigErrorStep = result.getResultConfig().getResults().get(errorStepPosition - 1);
        ToolTestcaseResultStep resultInfoErrorStep = resultInfo.getResults().get(errorStepPosition - 1);

        errorMsgStrBuilder.appendWithDelimiter(
            ToolMessageUtils.getMessage(this.msgSource, errorCode, errorStepPosition, resultConfigErrorStep.getDescription().getText(),
                resultConfigErrorStep.isSuccessful(), resultInfoErrorStep.isSuccessful()), StringUtils.LF);

        Object[] msgParams = new Object[] { hostingTestcase.getBindingType(), hostingTestcase.getLocationType() };

        if (!hostingTestcase.isNegative() && !(lastStep instanceof ToolTestcaseCertificateResultStep)) {
            errorMsgStrBuilder.appendWithDelimiter(
                ToolMessageUtils.getMessage(this.msgSource, ToolTestcaseCertificateResultType.MISSING_CERT.getMessage(), msgParams), StringUtils.LF);
        }

        if (resultInfoErrorStep.hasMessage()) {
            errorMsgStrBuilder.appendWithDelimiter(resultInfoErrorStep.getMessage(), StringUtils.LF);
        }

        return errorMsgStrBuilder.build();
    }

    private String getCertMessage(HostingTestcase hostingTestcase, ToolTestcaseCertificateResultStep certResultStep, boolean successful) {
        ToolTestcaseCertificateResultType certResultType = certResultStep.getCertificateStatus();
        Object[] certStepMsgParams = new Object[] { certResultStep.getBindingType(), certResultStep.getLocationType() };
        Object[] testcaseMsgParams = new Object[] { hostingTestcase.getBindingType(), hostingTestcase.getLocationType() };
        ToolStrBuilder certStrBuilder = new ToolStrBuilder();

        if (certResultType == ToolTestcaseCertificateResultType.NO_CERT) {
            certStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, certResultType.getMessage(), testcaseMsgParams), StringUtils.LF);
        } else {
            certStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, certResultType.getMessage(), certStepMsgParams), StringUtils.LF);
        }

        if (certResultStep.hasCertificateInfo() && !successful) {
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
