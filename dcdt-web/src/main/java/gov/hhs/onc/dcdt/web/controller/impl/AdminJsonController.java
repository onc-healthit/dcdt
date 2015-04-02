package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigJsonDto;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingJsonDto;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingService;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.controller.JsonRequest;
import gov.hhs.onc.dcdt.web.controller.JsonResponse;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import gov.hhs.onc.dcdt.web.service.ToolServiceHub;
import gov.hhs.onc.dcdt.web.service.ToolServiceHubJsonDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminJsonController")
@JsonController
@JsonResponse
public class AdminJsonController extends AbstractToolController {
    @Autowired
    private InstanceConfigRegistry instanceConfigRegistry;

    @Autowired
    private ToolServiceHub serviceHub;

    @JsonRequest
    @RequestMapping(value = { "/admin/mail/mappings" }, method = { RequestMethod.GET })
    public ResponseJsonWrapper<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingJsonDto> getMailMappings() throws Exception {
        return new ResponseJsonWrapperBuilder<DiscoveryTestcaseMailMapping, DiscoveryTestcaseMailMappingJsonDto>().addItems(this.createMailMappingsJsonDtos())
            .build();
    }

    @JsonRequest
    @RequestMapping(value = { "/admin/service/hub" }, method = { RequestMethod.GET })
    public ResponseJsonWrapper<ToolServiceHub, ToolServiceHubJsonDto> getServiceHub() throws Exception {
        return new ResponseJsonWrapperBuilder<ToolServiceHub, ToolServiceHubJsonDto>().addItems(this.createServiceHubJsonDto()).build();
    }

    @JsonRequest
    @RequestMapping({ "/admin/instance/set" })
    public ResponseJsonWrapper<InstanceConfig, InstanceConfigJsonDto> setInstanceConfig(
        @RequestBody @Validated RequestJsonWrapper<InstanceConfig, InstanceConfigJsonDto> reqJsonWrapper, BindingResult bindingResult) throws Exception {
        ResponseJsonWrapperBuilder<InstanceConfig, InstanceConfigJsonDto> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            InstanceConfigJsonDto reqInstanceConfigJsonDto = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqInstanceConfigJsonDto != null) {
                if (this.getExistingInstanceConfigBean() != null) {
                    this.removeInstanceConfig();
                }

                InstanceConfig reqInstanceConfig = reqInstanceConfigJsonDto.toBean(this.convService);

                InstanceConfig instanceConfig = this.getInstanceConfigBean();
                instanceConfig.setDomainName(reqInstanceConfig.getDomainName());
                instanceConfig.setIpAddress(reqInstanceConfig.getIpAddress());

                this.instanceConfigRegistry.registerBeans(instanceConfig);
            }
        }

        return respJsonWrapperBuilder.build();
    }

    @RequestMapping(value = { "/admin/instance/rm" }, method = { RequestMethod.POST })
    public ResponseJsonWrapper<InstanceConfig, InstanceConfigJsonDto> removeInstanceConfig() throws Exception {
        this.instanceConfigRegistry.removeAllBeans();

        return this.getInstanceConfig();
    }

    @RequestMapping(value = { "/admin/instance" }, method = { RequestMethod.GET })
    public ResponseJsonWrapper<InstanceConfig, InstanceConfigJsonDto> getInstanceConfig() throws Exception {
        return new ResponseJsonWrapperBuilder<InstanceConfig, InstanceConfigJsonDto>().addItems(this.createInstanceConfigJsonDto(this.getInstanceConfigBean()))
            .build();
    }

    private List<DiscoveryTestcaseMailMappingJsonDto> createMailMappingsJsonDtos() throws Exception {
        // noinspection ConstantConditions
        List<DiscoveryTestcaseMailMapping> mailMappings =
            ToolBeanFactoryUtils.getBeanOfType(this.appContext, DiscoveryTestcaseMailMappingService.class).getBeans();
        List<DiscoveryTestcaseMailMappingJsonDto> mailMappingsJsonDtos = new ArrayList<>(mailMappings.size());
        DiscoveryTestcaseMailMappingJsonDto mailMappingsJsonDto;

        for (DiscoveryTestcaseMailMapping mailMapping : mailMappings) {
            // noinspection ConstantConditions
            (mailMappingsJsonDto = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DiscoveryTestcaseMailMappingJsonDto.class)).fromBean(
                this.convService, mailMapping);
            mailMappingsJsonDtos.add(mailMappingsJsonDto);
        }

        return mailMappingsJsonDtos;
    }

    private ToolServiceHubJsonDto createServiceHubJsonDto() throws Exception {
        ToolServiceHubJsonDto serviceHubJsonDto = ToolBeanFactoryUtils.createBeanOfType(this.appContext, ToolServiceHubJsonDto.class);
        // noinspection ConstantConditions
        serviceHubJsonDto.fromBean(this.convService, this.serviceHub);

        return serviceHubJsonDto;
    }

    private InstanceConfigJsonDto createInstanceConfigJsonDto(InstanceConfig instanceConfig) throws Exception {
        InstanceConfigJsonDto instanceConfigJsonDto = ToolBeanFactoryUtils.createBeanOfType(this.appContext, InstanceConfigJsonDto.class);
        // noinspection ConstantConditions
        instanceConfigJsonDto.fromBean(this.convService, instanceConfig);

        return instanceConfigJsonDto;
    }

    private InstanceConfig getInstanceConfigBean() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class);
    }

    private InstanceConfig getExistingInstanceConfigBean() {
        // noinspection ConstantConditions
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfigService.class).getBean();
    }
}
