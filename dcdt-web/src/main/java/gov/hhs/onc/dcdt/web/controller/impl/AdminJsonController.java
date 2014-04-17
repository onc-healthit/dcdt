package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigJsonDto;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
import gov.hhs.onc.dcdt.context.LifecycleStatusType;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.controller.JsonRequest;
import gov.hhs.onc.dcdt.web.controller.JsonResponse;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminJsonController.class);

    @Autowired
    private InstanceConfigRegistry instanceConfigRegistry;

    private final static String SERVICES_MSG_CODE = "dcdt.web.admin.instance.config.services.msg";

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

                startServices();

                InstanceConfigJsonDto instanceConfigJsonDto = this.getInstanceConfigJsonDto(instanceConfig);
                // noinspection ConstantConditions
                instanceConfigJsonDto.setMessage(ToolMessageUtils.getMessage(this.msgSource, SERVICES_MSG_CODE,
                    ToolBeanFactoryUtils.getBeanOfType(this.appContext, DnsService.class).getLifecycleStatus(),
                    ToolBeanFactoryUtils.getBeanOfType(this.appContext, LdapService.class).getLifecycleStatus(),
                    ToolBeanFactoryUtils.getBeanOfType(this.appContext, MailService.class).getLifecycleStatus()));

                respJsonWrapperBuilder.addItems(instanceConfigJsonDto);
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
        return new ResponseJsonWrapperBuilder<InstanceConfig, InstanceConfigJsonDto>().addItems(this.getInstanceConfigJsonDto(this.getInstanceConfigBean()))
            .build();
    }

    private InstanceConfigJsonDto getInstanceConfigJsonDto(InstanceConfig instanceConfig) throws Exception {
        InstanceConfigJsonDto instanceConfigJsonDto =
            this.appContext.getBean(ToolBeanFactoryUtils.getBeanNameOfType(this.appContext, InstanceConfigJsonDto.class), InstanceConfigJsonDto.class);
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

    private void startServices() {
        boolean serviceNotStarted = false;

        for (ToolService service : this.getServices()) {
            service.start();

            if (service.getLifecycleStatus() != LifecycleStatusType.STARTED) {
                serviceNotStarted = true;
                LOGGER.error(String.format("Service (class=%s, beanName=%s) was not started, status=%s.", ToolClassUtils.getName(service.getClass()),
                    service.getBeanName(), service.getLifecycleStatus()));
                break;
            }
        }

        if (serviceNotStarted) {
            for (ToolService service : this.getServices()) {
                service.stop();
            }
        }
    }

    private List<ToolService> getServices() {
        return ToolBeanFactoryUtils.getBeansOfType(this.appContext, ToolService.class);
    }
}
