package gov.hhs.onc.dcdt.service.test.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolDateUtils;
import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@ContextConfiguration({ "spring/spring-service.xml", "spring/spring-service-standalone.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.all" })
public abstract class AbstractToolServiceFunctionalTests<T extends ToolService> extends AbstractToolFunctionalTests {
    protected static boolean testServiceInstanceConfigRegistered;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected InstanceConfigRegistry instanceConfigReg;

    @Value("${dcdt.test.service.instance.domain.name}")
    protected Name testServiceInstanceConfigDomainName;

    @Value("${dcdt.test.service.instance.ip.addr}")
    protected InetAddress testServiceInstanceConfigIpAddr;

    protected Class<T> serviceClass;
    protected T service;

    protected AbstractToolServiceFunctionalTests(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @AfterGroups(groups = { "dcdt.test.func.service.all" }, alwaysRun = true, timeOut = ToolDateUtils.MS_IN_SEC * 30)
    public void stopService() {
        if (this.service != null) {
            this.service.stop();
        }
    }

    public void startService() {
        (this.service = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, this.serviceClass)).start();
    }

    @BeforeClass(groups = { "dcdt.test.func.service.all" })
    public void registerInstanceConfig() {
        if (!testServiceInstanceConfigRegistered) {
            InstanceConfig instanceConfig = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, InstanceConfig.class);
            instanceConfig.setDomainName(this.testServiceInstanceConfigDomainName);
            instanceConfig.setIpAddress(this.testServiceInstanceConfigIpAddr);

            this.instanceConfigReg.registerBeans(instanceConfig);

            testServiceInstanceConfigRegistered = true;
        }
    }
}
