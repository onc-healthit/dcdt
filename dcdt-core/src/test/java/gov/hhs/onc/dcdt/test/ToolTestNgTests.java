package gov.hhs.onc.dcdt.test;

import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import gov.hhs.onc.dcdt.context.ToolMessageSource;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolIterableUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

@ContextConfiguration(loader = ToolTestNgContextLoader.class, locations = { "spring/spring-core.xml", "spring/spring-core-*.xml" })
public abstract class ToolTestNgTests extends AbstractTestNGSpringContextTests implements BeanDefinitionRegistryAware, BeanFactoryAware, InitializingBean,
    MessageSourceAware {
    /**
     * @see org.springframework.test.context.testng.AbstractTestNGSpringContextTests.applicationContext
     */
    protected AbstractApplicationContext applicationContext;

    protected BeanDefinitionRegistry beanDefReg;
    protected ConfigurableListableBeanFactory beanFactory;
    protected MessageSource msgSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.applicationContext = (AbstractApplicationContext) super.applicationContext;
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
            dataGroupBeans.add(ToolBeanFactoryUtils.getBeanOfType(this.beanFactory, dataGroupBeanClass));
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
    public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) {
        this.beanDefReg = beanDefReg;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void setMessageSource(MessageSource msgSource) {
        this.msgSource = msgSource;
    }
}
