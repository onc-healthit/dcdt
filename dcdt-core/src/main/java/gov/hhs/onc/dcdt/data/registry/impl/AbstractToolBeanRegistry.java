package gov.hhs.onc.dcdt.data.registry.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistryException;
import gov.hhs.onc.dcdt.data.registry.ToolBeanRegistry;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
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
    protected V beanService;
    protected Class<T> beanClass;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolBeanRegistry.class);

    protected AbstractToolBeanRegistry(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public void processBeans() throws ToolBeanRegistryException {
        for (T bean : this.getBeans()) {
            try {
                this.processBean(bean);

                LOGGER.debug(String.format("Processed bean (class=%s, beanName=%s, beanId=%s).", ToolClassUtils.getName(bean), bean.getBeanName(),
                    bean.getBeanId()));
            } catch (Throwable th) {
                throw new ToolBeanRegistryException(String.format("Unable to process bean (class=%s, beanName=%s, beanId=%s).", ToolClassUtils.getName(bean),
                    bean.getBeanName(), bean.getBeanId()), th);
            }
        }

        this.appContext.refresh();
    }

    protected void processBean(T bean) throws ToolBeanRegistryException {
        this.beanService.setBean(bean);
    }

    protected List<T> getBeans() {
        List<T> beans = ToolBeanFactoryUtils.getBeansOfType(this.appContext.getBeanFactory(), this.beanClass);

        for (T bean : beans) {
            this.beanService.updateBean(bean);
        }

        return beans;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractRefreshableApplicationContext) appContext;
    }

    protected void setBeanService(V beanService) {
        this.beanService = beanService;
    }
}
