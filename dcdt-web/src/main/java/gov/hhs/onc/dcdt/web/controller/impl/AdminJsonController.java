package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
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
    public ResponseJsonWrapper<InstanceConfig> setInstanceConfig(@RequestBody @Validated RequestJsonWrapper<InstanceConfig> reqJsonWrapper,
        BindingResult bindingResult) {
        ResponseJsonWrapperBuilder<InstanceConfig> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();
        respJsonWrapperBuilder.addBindingErrors(this.msgSourceValidation, bindingResult);

        if (!bindingResult.hasErrors()) {
            InstanceConfig reqInstanceConfig = ToolListUtils.getFirst(reqJsonWrapper.getItems());

            if (reqInstanceConfig != null) {
                InstanceConfig instanceConfig = this.getInstanceConfigBean();
                instanceConfig.setDomainName(reqInstanceConfig.getDomainName());
                instanceConfig.setIpAddress(reqInstanceConfig.getIpAddress());

                this.instanceConfigRegistry.registerBeans(instanceConfig);

                respJsonWrapperBuilder.addItems(this.getInstanceConfigBean());
            }
        }

        return respJsonWrapperBuilder.build();
    }

    @RequestMapping(value = { "/admin/instance/rm" }, method = { RequestMethod.POST })
    public ResponseJsonWrapper<InstanceConfig> removeInstanceConfig() {
        ResponseJsonWrapperBuilder<InstanceConfig> respJsonWrapperBuilder = new ResponseJsonWrapperBuilder<>();

        this.instanceConfigRegistry.removeAllBeans();

        respJsonWrapperBuilder.addItems(this.getInstanceConfigBean());

        return respJsonWrapperBuilder.build();
    }

    @RequestMapping(value = { "/admin/instance" }, method = { RequestMethod.GET })
    public ResponseJsonWrapper<InstanceConfig> getInstanceConfig() {
        return new ResponseJsonWrapperBuilder<InstanceConfig>().addItems(this.getInstanceConfigBean()).build();
    }

    private InstanceConfig getInstanceConfigBean() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class);
    }
}
