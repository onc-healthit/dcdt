package gov.hhs.onc.dcdt.data.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanPostProcessor;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import javax.annotation.Nullable;

public abstract class AbstractLoadingBeanPostProcessor<T extends ToolBean, U extends ToolBeanDao<T>, V extends ToolBeanService<T, U>> extends
    AbstractToolBeanPostProcessor<T> {
    protected Class<V> beanServiceClass;

    protected AbstractLoadingBeanPostProcessor(Class<T> beanClass, Class<V> beanServiceClass) {
        super(beanClass, true, false);

        this.beanServiceClass = beanServiceClass;
    }

    @Override
    protected T postProcessBeforeInitializationInternal(T bean, String beanName) throws Exception {
        // noinspection ConstantConditions
        T persistentBean = this.findPersistentBean(bean, ToolBeanFactoryUtils.getBeanOfType(this.appContext, this.beanServiceClass));

        // noinspection ConstantConditions
        return ((persistentBean != null) ? this.loadBean(bean, persistentBean) : bean);
    }

    protected T loadBean(T bean, T persistentBean) throws Exception {
        bean.afterPropertiesSet();

        return bean;
    }

    @Nullable
    protected abstract T findPersistentBean(T bean, V beanService) throws Exception;
}
