package gov.hhs.onc.dcdt.data.registry.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistry;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanRegistry<T extends ToolBean, U extends ToolBeanDao<T>, V extends ToolBeanService<T, U>> implements
    ToolBeanRegistry<T, U, V> {
    protected AbstractRefreshableApplicationContext appContext;
    protected Class<T> beanClass;
    protected Class<V> beanServiceClass;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanRegistry.class);

    protected AbstractToolBeanRegistry(Class<T> beanClass, Class<V> beanServiceClass) {
        this.beanClass = beanClass;
        this.beanServiceClass = beanServiceClass;
    }

    @Override
    public void registerAllBeans() throws ToolBeanRegistryException {
        this.registerBeans(this.getBeans());
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void registerBeans(T ... beans) throws ToolBeanRegistryException {
        this.registerBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public void registerBeans(Iterable<T> beans) throws ToolBeanRegistryException {
        for (T bean : beans) {
            try {
                this.registerBean(bean);

                LOGGER.debug(String.format("Registered bean (class=%s, beanName=%s, beanId=%s).", ToolClassUtils.getName(bean), bean.getBeanName(),
                    bean.getBeanId()));
            } catch (Throwable th) {
                throw new ToolBeanRegistryException(String.format("Unable to register bean (class=%s, beanName=%s, beanId=%s).", ToolClassUtils.getName(bean),
                    bean.getBeanName(), bean.getBeanId()), th);
            }
        }

        this.appContext.refresh();
    }

    @Override
    public void registerBean(T bean) throws ToolBeanRegistryException {
        this.getBeanService().setBean(bean);
    }

    @Override
    public void removeAllBeans() throws ToolBeanRegistryException {
        this.removeBeans(this.getBeans());
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void removeBeans(T ... beans) throws ToolBeanRegistryException {
        this.removeBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public void removeBeans(Iterable<T> beans) throws ToolBeanRegistryException {
        for (T bean : beans) {
            try {
                this.removeBean(bean);

                LOGGER.debug(String.format("Removed bean (class=%s, beanName=%s, beanId=%s).", ToolClassUtils.getName(bean), bean.getBeanName(),
                    bean.getBeanId()));
            } catch (Throwable th) {
                throw new ToolBeanRegistryException(String.format("Unable to remove bean (class=%s, beanName=%s, beanId=%s).", ToolClassUtils.getName(bean),
                    bean.getBeanName(), bean.getBeanId()), th);
            }
        }

        this.appContext.refresh();
    }

    @Override
    public void removeBean(T bean) throws ToolBeanRegistryException {
        this.getBeanService().removeBean(bean);
    }

    protected V getBeanService() {
        return ToolBeanFactoryUtils.getBeanOfType(this.appContext.getBeanFactory(), this.beanServiceClass);
    }

    protected List<T> getBeans() {
        return ToolBeanFactoryUtils.getBeansOfType(this.appContext.getBeanFactory(), this.beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractRefreshableApplicationContext) appContext;
    }
}
