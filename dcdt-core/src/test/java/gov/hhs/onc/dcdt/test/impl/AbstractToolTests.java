package gov.hhs.onc.dcdt.test.impl;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolIterableUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.AbstractApplicationContext;
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

    protected Iterator<Object[]> getDataGroupsBeans(Iterable<Class<?>[]> dataGroupsBeanClasses) {
        List<Object[]> dataGroupsBeans = new ArrayList<>();

        for (Class<?>[] dataGroupBeanClasses : dataGroupsBeanClasses) {
            dataGroupsBeans.add(this.getDataGroupBeansList(ToolArrayUtils.asList(dataGroupBeanClasses)).toArray());
        }

        return this.getDataGroups(dataGroupsBeans);
    }

    protected Iterator<Object[]> getDataGroupBeans(Class<?> ... dataGroupBeanClasses) {
        return this.getDataGroupBeans(ToolArrayUtils.asList(dataGroupBeanClasses));
    }

    protected Iterator<Object[]> getDataGroupBeans(Iterable<Class<?>> dataGroupBeanClasses) {
        return this.getDataGroup(this.getDataGroupBeansList(dataGroupBeanClasses));
    }

    protected List<Object> getDataGroupBeansList(Iterable<Class<?>> dataGroupBeanClasses) {
        List<Object> dataGroupBeans = new ArrayList<>();

        for (Class<?> dataGroupBeanClass : dataGroupBeanClasses) {
            dataGroupBeans.add(ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, dataGroupBeanClass));
        }

        return dataGroupBeans;
    }

    protected Iterator<Object[]> getDataGroup(Object ... dataGroup) {
        return this.getDataGroup(ToolArrayUtils.asList(dataGroup));
    }

    protected Iterator<Object[]> getDataGroup(Iterable<Object> dataGroup) {
        return ToolIterableUtils.wrapElements(dataGroup.iterator());
    }

    protected Iterator<Object[]> getDataGroups(Object[] ... dataGroups) {
        return this.getDataGroups(ToolArrayUtils.asList(dataGroups));
    }

    protected Iterator<Object[]> getDataGroups(Iterable<Object[]> dataGroups) {
        return ToolIterableUtils.wrapElements(dataGroups.iterator());
    }

    @Override
    public void setMessageSource(MessageSource msgSource) {
        this.msgSource = msgSource;
    }
}
