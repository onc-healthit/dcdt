package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingJsonDto;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingRegistry;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
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

@Controller("discoveryMailMappingJsonController")
@JsonController
@JsonResponse
public class DiscoveryMailMappingJsonController extends AbstractToolController {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseMailMappingRegistry discoveryTestcaseMailMappingRegistry;

    @JsonRequest
    @RequestMapping({ "/discovery/mail/mapping/add" })
    public ResponseJsonWrapper<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingJsonDto> setDiscoveryTestcaseMailMapping(
        @RequestBody @Validated RequestJsonWrapper<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingJsonDto> reqJsonWrapper,
        BindingResult bindingResult) throws Exception {
        String successMsgCode = "dcdt.web.discovery.mail.mapping.submission.success";

        ResponseJsonWrapperBuilder<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingJsonDto> respJsonWrapperBuilder =
            new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            DiscoveryTestcaseMailMappingJsonDto reqDiscoveryTestcaseMailMappingJsonDto = ToolListUtils.getFirst(reqJsonWrapper.getItems());
            if (reqDiscoveryTestcaseMailMappingJsonDto != null) {
                DiscoveryTestcaseMailMapping reqDiscoveryTestcaseMailMapping = reqDiscoveryTestcaseMailMappingJsonDto.toBean(this.convService);

                MailAddress directAddr = reqDiscoveryTestcaseMailMapping.getDirectAddress();
                MailAddress resultsAddr = reqDiscoveryTestcaseMailMapping.getResultsAddress();

                DiscoveryTestcaseMailMapping discoveryTestcaseMailMapping =
                    ToolBeanFactoryUtils.createBeanOfType(this.appContext, DiscoveryTestcaseMailMapping.class);
                // noinspection ConstantConditions
                discoveryTestcaseMailMapping.setDirectAddress(directAddr);
                discoveryTestcaseMailMapping.setResultsAddress(resultsAddr);

                this.discoveryTestcaseMailMappingRegistry.registerBeans(discoveryTestcaseMailMapping);

                DiscoveryTestcaseMailMappingJsonDto discoveryTestcaseMailMappingJsonDto =
                    this.getDiscoveryTestcaseMailMappingJsonDto(discoveryTestcaseMailMapping);
                // noinspection ConstantConditions
                discoveryTestcaseMailMappingJsonDto.setMessage(ToolMessageUtils.getMessage(this.msgSource, successMsgCode,
                    new Object[] { directAddr.toAddress(), resultsAddr.toAddress() }));

                respJsonWrapperBuilder.addItems(discoveryTestcaseMailMappingJsonDto);
            }
        }

        return respJsonWrapperBuilder.build();
    }

    private DiscoveryTestcaseMailMappingJsonDto getDiscoveryTestcaseMailMappingJsonDto(DiscoveryTestcaseMailMapping discoveryTestcaseMailMapping)
        throws Exception {
        DiscoveryTestcaseMailMappingJsonDto discoveryTestcaseMailMappingJsonDto =
            this.appContext.getBean(ToolBeanFactoryUtils.getBeanNameOfType(this.appContext, DiscoveryTestcaseMailMappingJsonDto.class),
                DiscoveryTestcaseMailMappingJsonDto.class);
        discoveryTestcaseMailMappingJsonDto.fromBean(this.convService, discoveryTestcaseMailMapping);

        return discoveryTestcaseMailMappingJsonDto;
    }
}
