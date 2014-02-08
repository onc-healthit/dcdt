package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigJsonDto;
import gov.hhs.onc.dcdt.config.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.controller.JsonRequest;
import gov.hhs.onc.dcdt.web.controller.JsonResponse;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminJsonController")
@JsonController
@JsonResponse
@Scope("singleton")
public class AdminJsonController extends AbstractToolController {
    @Autowired
    private InstanceConfigRegistry instanceConfigRegistry;

    @JsonRequest
    @RequestMapping({ "/admin/instance/set" })
    public ResponseJsonWrapper<InstanceConfig, InstanceConfigJsonDto> setInstanceConfig(
        @RequestBody @Validated RequestJsonWrapper<InstanceConfig, InstanceConfigJsonDto> reqJsonWrapper, BindingResult bindingResult) throws Exception {
        ResponseJsonWrapperBuilder<InstanceConfig, InstanceConfigJsonDto> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            InstanceConfigJsonDto reqInstanceConfigJsonDto = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqInstanceConfigJsonDto != null) {
                InstanceConfig reqInstanceConfig = reqInstanceConfigJsonDto.toBean(this.convService);

                InstanceConfig instanceConfig = this.getInstanceConfigBean();
                instanceConfig.setDomainName(reqInstanceConfig.getDomainName());
                instanceConfig.setIpAddress(reqInstanceConfig.getIpAddress());

                this.instanceConfigRegistry.registerBeans(instanceConfig);

                respJsonWrapperBuilder.addItems(this.getInstanceConfigJsonDto(this.getInstanceConfigBean()));
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
}
