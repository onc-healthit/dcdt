package gov.hhs.onc.dcdt.test.impl;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(loader = ToolContextLoaderImpl.class, locations = { "spring/spring-core.xml", "spring/spring-core-*.xml" })
@Test(groups = { "dcdt.test.all" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class }, inheritListeners = false)
public abstract class AbstractToolTests extends AbstractTestNGSpringContextTests implements InitializingBean, MessageSourceAware {
    @Resource(name = "conversionService")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected FormattingConversionService convService;

    /**
     * @see org.springframework.test.context.testng.AbstractTestNGSpringContextTests.applicationContext
     */
    protected AbstractApplicationContext applicationContext;

    protected MessageSource msgSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.applicationContext = (AbstractApplicationContext) super.applicationContext;
    }

    protected void assertMessageEquals(String str, String msgCode, String assertMsg) {
        Assert.assertEquals(str, ToolMessageUtils.getMessage(this.msgSource, msgCode), assertMsg);
    }

    @Override
    public void setMessageSource(MessageSource msgSource) {
        this.msgSource = msgSource;
    }
}
