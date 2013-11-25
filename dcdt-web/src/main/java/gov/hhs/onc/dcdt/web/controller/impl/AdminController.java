package gov.hhs.onc.dcdt.web.controller.impl;


import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.RequestView;
import gov.hhs.onc.dcdt.web.controller.RequestViews;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperImpl;
import java.io.File;
import javax.validation.Valid;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminController")
@Scope("singleton")
public class AdminController extends AbstractToolController implements ApplicationContextAware, BeanDefinitionRegistryAware {
    @Autowired(required = false)
    private InstanceConfig instanceConfig;

    private AbstractRefreshableApplicationContext appContext;
    private BeanDefinitionRegistry beanDefReg;

    @RequestMapping(value = { "/admin/instance/set" }, method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseJsonWrapper<InstanceConfig> setInstanceConfiguration(@RequestBody @Valid RequestJsonWrapper<InstanceConfig> req, BindingResult bindingResult) {
        InstanceConfig reqInstanceConfig = ToolListUtils.getFirst(req.getItems());
        File instanceConfigDir = reqInstanceConfig.getDirectory();
        String instanceConfigDomain = reqInstanceConfig.getDomain();

        if (this.instanceConfig != null) {
            this.instanceConfig.setDirectory(instanceConfigDir);
            this.instanceConfig.setDomain(instanceConfigDomain);
        } else {
            this.instanceConfig = reqInstanceConfig;
        }

        AdminBeanDefinitionRegistryPostProcessor.instanceConfig = this.instanceConfig;

        this.appContext.refresh();

        ResponseJsonWrapper<InstanceConfig> resp = new ResponseJsonWrapperImpl<>();

        return resp;
    }

    @RequestMapping(value = { "/admin/instance/rm" }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseJsonWrapper<InstanceConfig> removeInstanceConfiguration() {
        if (this.instanceConfig != null) {
            beanDefReg.removeBeanDefinition(this.instanceConfig.getBeanName());

            this.appContext.refresh();
        }

        ResponseJsonWrapper<InstanceConfig> resp = new ResponseJsonWrapperImpl<>();

        return resp;
    }

    @RequestMapping(value = { "/admin/instance" }, method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseJsonWrapper<InstanceConfig> getInstanceConfiguration() {
        ResponseJsonWrapper<InstanceConfig> resp = new ResponseJsonWrapperImpl<>();
        resp.getItems().add(this.instanceConfig);

        return resp;
    }

    @RequestMapping(value = { "/admin" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("admin") })
    public ModelAndView displayAdmin(ModelMap modelMap) throws ToolWebException {
        return this.display(modelMap);
    }

    @RequestMapping(value = { "/admin/login" }, method = { RequestMethod.GET })
    @RequestViews({ @RequestView("admin-login") })
    public ModelAndView displayAdminLogin(ModelMap modelMap) throws ToolWebException {
        return this.display(modelMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractRefreshableApplicationContext) appContext;
    }

    @Override
    public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) {
        this.beanDefReg = beanDefReg;
    }
}
