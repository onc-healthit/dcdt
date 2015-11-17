package gov.hhs.onc.dcdt.service.test.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
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
public abstract class AbstractToolServiceFunctionalTests<T extends TransportProtocol, U extends ToolServerConfig<T>, V extends ToolServer<T, U>, W extends ToolService<T, U, V>>
    extends AbstractToolFunctionalTests {
    protected static boolean testServiceInstanceConfigRegistered;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected InstanceConfigRegistry instanceConfigReg;

    @Value("${dcdt.test.service.instance.domain.name}")
    protected Name testServiceInstanceConfigDomainName;

    @Value("${dcdt.test.service.instance.ip.addr}")
    protected InetAddress testServiceInstanceConfigIpAddr;

    protected Class<W> serviceClass;
    protected W service;

    protected AbstractToolServiceFunctionalTests(Class<W> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @AfterGroups(groups = { "dcdt.test.func.service.all" }, alwaysRun = true, timeOut = ToolDateUtils.MS_IN_SEC * 30)
    public void stopService() {
        if (this.service != null) {
            this.service.stop();
        }
    }

    public void startService() {
        // noinspection ConstantConditions
        (this.service = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, this.serviceClass)).start();
    }

    @BeforeClass(groups = { "dcdt.test.func.service.all" })
    public void registerInstanceConfig() {
        if (!testServiceInstanceConfigRegistered) {
            InstanceConfig instanceConfig = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, InstanceConfig.class);
            // noinspection ConstantConditions
            instanceConfig.setDomainName(this.testServiceInstanceConfigDomainName);
            instanceConfig.setIpAddress(this.testServiceInstanceConfigIpAddr);

            this.instanceConfigReg.registerBeans(instanceConfig);

            testServiceInstanceConfigRegistered = true;
        }
    }
}
