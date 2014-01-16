package gov.hhs.onc.dcdt.data.tx.services.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessException;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class AbstractToolBeanService<T extends ToolBean, U extends ToolBeanDao<T>> extends AbstractToolBean implements ToolBeanService<T, U> {
    protected U beanDao;

    @Override
    @SuppressWarnings({ "unchecked" })
    public boolean containsBeans(Serializable ... beanIdValues) throws ToolBeanDataAccessException {
        return this.beanDao.containsBeans(beanIdValues);
    }

    @Override
    public boolean containsBeans(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException {
        return this.beanDao.containsBeans(beanIdValues);
    }

    @Override
    public boolean containsBean(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.beanDao.containsBean(beanIdValue);
    }

    @Override
    public T getFirstBean() throws ToolBeanDataAccessException {
        return this.beanDao.getFirstBean();
    }

    @Override
    public List<T> getAllBeans() throws ToolBeanDataAccessException {
        return this.beanDao.getAllBeans();
    }

    @Override
    public List<T> getBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException {
        return this.beanDao.getBeansById(beanIdValues);
    }

    @Override
    public List<T> getBeansById(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException {
        return this.beanDao.getBeansById(beanIdValues);
    }

    @Override
    public T getBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.beanDao.getBeanById(beanIdValue);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> getBeans(Pair<String, ? extends Serializable> ... beanColumnPairs) throws ToolBeanDataAccessException {
        return this.beanDao.getBeans(beanColumnPairs);
    }

    @Override
    public List<T> getBeans(Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        return this.beanDao.getBeans(beanColumnPairs);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public T getBean(Pair<String, ? extends Serializable> ... beanColumnPairs) throws ToolBeanDataAccessException {
        return this.beanDao.getBean(beanColumnPairs);
    }

    @Override
    public T getBean(Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        return this.beanDao.getBean(beanColumnPairs);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> loadBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.beanDao.loadBeans(beans);
    }

    @Override
    public List<T> loadBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        return this.beanDao.loadBeans(beans);
    }

    @Override
    public T loadBean(T bean) throws ToolBeanDataAccessException {
        return this.beanDao.loadBean(bean);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> refreshBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.beanDao.refreshBeans(beans);
    }

    @Override
    public List<T> refreshBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        return this.beanDao.refreshBeans(beans);
    }

    @Override
    public T refreshBean(T bean) throws ToolBeanDataAccessException {
        return this.beanDao.refreshBean(bean);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    @Transactional(readOnly = false)
    public List<T> setBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.beanDao.setBeans(beans);
    }

    @Override
    @Transactional(readOnly = false)
    public List<T> setBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        return this.beanDao.setBeans(beans);
    }

    @Override
    @Transactional(readOnly = false)
    public T setBean(T bean) throws ToolBeanDataAccessException {
        return this.beanDao.setBean(bean);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    @Transactional(readOnly = false)
    public List<T> addBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.beanDao.addBeans(beans);
    }

    @Override
    @Transactional(readOnly = false)
    public List<T> addBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        return this.beanDao.addBeans(beans);
    }

    @Override
    @Transactional(readOnly = false)
    public T addBean(T bean) throws ToolBeanDataAccessException {
        return this.beanDao.addBean(bean);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    @Transactional(readOnly = false)
    public List<T> updateBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.beanDao.updateBeans(beans);
    }

    @Override
    @Transactional(readOnly = false)
    public List<T> updateBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        return this.beanDao.updateBeans(beans);
    }

    @Override
    @Transactional(readOnly = false)
    public T updateBean(T bean) throws ToolBeanDataAccessException {
        return this.beanDao.updateBean(bean);
    }

    @Override
    @Transactional(readOnly = false)
    public List<T> removeBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException {
        return this.beanDao.removeBeansById(beanIdValues);
    }

    @Override
    @Transactional(readOnly = false)
    public List<T> removeBeansById(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException {
        return this.beanDao.removeBeansById(beanIdValues);
    }

    @Override
    @Transactional(readOnly = false)
    public T removeBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.beanDao.removeBeanById(beanIdValue);
    }

    @SuppressWarnings({ "unchecked" })
    @Transactional(readOnly = false)
    public List<T> removeBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.beanDao.removeBeans(beans);
    }

    @Transactional(readOnly = false)
    public List<T> removeBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        return this.beanDao.removeBeans(beans);
    }

    @Transactional(readOnly = false)
    public T removeBean(T bean) throws ToolBeanDataAccessException {
        return this.beanDao.removeBean(bean);
    }

    protected void setBeanDao(U beanDao) {
        this.beanDao = beanDao;
    }
}
