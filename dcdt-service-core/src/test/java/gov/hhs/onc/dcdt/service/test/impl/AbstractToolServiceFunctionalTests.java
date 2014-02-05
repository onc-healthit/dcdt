package gov.hhs.onc.dcdt.service.test.impl;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import javax.annotation.Resource;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service.xml", "spring/spring-service-standalone.xml" })
@Test(groups = { "dcdt.test.func.service.all" })
public abstract class AbstractToolServiceFunctionalTests<T extends ToolService> extends AbstractToolFunctionalTests {
    @Resource(name = "toolServiceTaskExecutor")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected ThreadPoolTaskExecutor serviceTaskExecutor;

    protected Class<T> serviceClass;
    protected T service;

    protected AbstractToolServiceFunctionalTests(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @BeforeClass(groups = { "dcdt.test.func.service.all" }, timeOut = DateUtils.MILLIS_PER_SECOND * 30)
    public void startTestService() throws Exception {
        if (this.service == null) {
            this.service = this.createService();
            this.serviceTaskExecutor.execute(this.service);
        }
    }

    @AfterGroups(groups = { "dcdt.test.func.service.all" }, alwaysRun = true, timeOut = DateUtils.MILLIS_PER_SECOND * 30)
    public void stopTestService() throws Exception {
        if (this.service != null) {
            this.service.stop();
        }
    }

    protected abstract T createService();
}
