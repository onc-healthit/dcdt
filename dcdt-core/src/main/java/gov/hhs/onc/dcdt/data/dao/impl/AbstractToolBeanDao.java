package gov.hhs.onc.dcdt.data.dao.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessException;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanDefinitionUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanDao<T extends ToolBean> implements ToolBeanDao<T> {
    @Autowired(required = false)
    protected SessionFactory sessionFactory;

    protected BeanDefinitionRegistry beanDefReg;
    protected ListableBeanFactory beanFactory;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractToolBeanDao(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    @SuppressWarnings({ "varargs" })
    public final boolean containsBeans(Serializable ... beanIdValues) throws ToolBeanDataAccessException {
        return this.containsBeans(ToolArrayUtils.asList(beanIdValues));
    }

    @Override
    public boolean containsBeans(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();

        for (Serializable beanIdValue : beanIdValues) {
            if (!this.containsBean(session, beanIdValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean containsBean(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.containsBean(this.getCheckedSession(), beanIdValue);
    }

    @Override
    @SuppressWarnings({ "varargs" })
    public final List<T> getBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException {
        return this.getBeansById(ToolArrayUtils.asList(beanIdValues));
    }

    @Override
    public List<T> getBeansById(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beans = new ArrayList<>();
        T bean;

        for (Serializable beanIdValue : beanIdValues) {
            if ((bean = this.getBeanById(session, beanIdValue)) != null) {
                beans.add(bean);
            }
        }

        return beans;
    }

    @Override
    public T getBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.getBeanById(this.getCheckedSession(), beanIdValue);
    }

    @Override
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final List<T> getBeans(Pair<String, ? extends Serializable> ... beanColumnPairs) throws ToolBeanDataAccessException {
        return this.getBeans(ToolArrayUtils.asList(beanColumnPairs));
    }

    @Override
    public List<T> getBeans(Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        return this.getBeans(this.getCheckedSession(), beanColumnPairs);
    }

    @Override
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final T getBean(Pair<String, ? extends Serializable> ... beanColumnPairs) throws ToolBeanDataAccessException {
        return this.getBean(ToolArrayUtils.asList(beanColumnPairs));
    }

    @Override
    public T getBean(Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        return this.getBean(this.getCheckedSession(), beanColumnPairs);
    }

    @Override
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final List<T> setBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.setBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public List<T> setBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansSet = new ArrayList<>();

        for (T bean : beans) {
            beansSet.add(this.setBean(session, bean));
        }

        return beansSet;
    }

    @Override
    public T setBean(T bean) throws ToolBeanDataAccessException {
        return this.setBean(this.getCheckedSession(), bean);
    }

    @Override
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final List<T> addBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.addBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public List<T> addBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansAdded = new ArrayList<>();

        for (T bean : beans) {
            beansAdded.add(this.addBean(session, bean));
        }

        return beansAdded;
    }

    @Override
    public T addBean(T bean) throws ToolBeanDataAccessException {
        return this.addBean(this.getCheckedSession(), bean);
    }

    @Override
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final List<T> updateBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.updateBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public List<T> updateBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansUpdated = new ArrayList<>();

        for (T bean : beans) {
            beansUpdated.add(this.updateBean(session, bean));
        }

        return beansUpdated;
    }

    @Override
    public T updateBean(T bean) throws ToolBeanDataAccessException {
        return this.updateBean(this.getCheckedSession(), bean);
    }

    @Override
    public List<T> removeBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException {
        return this.removeBeansById(ToolArrayUtils.asList(beanIdValues));
    }

    @Override
    public List<T> removeBeansById(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansRemoved = new ArrayList<>();

        for (Serializable beanIdValue : beanIdValues) {
            beansRemoved.add(this.removeBeanById(session, beanIdValue));
        }

        return beansRemoved;
    }

    @Override
    public T removeBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.removeBeanById(this.getCheckedSession(), beanIdValue);
    }

    @Override
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final List<T> removeBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.removeBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public List<T> removeBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansRemoved = new ArrayList<>();

        for (T bean : beans) {
            beansRemoved.add(this.removeBean(session, bean));
        }

        return beansRemoved;
    }

    @Override
    public T removeBean(T bean) throws ToolBeanDataAccessException {
        return this.removeBean(this.getCheckedSession(), bean);
    }

    @Override
    public boolean hasSession() {
        return this.getSession() != null;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void afterPropertiesSet() throws Exception {
        this.beanImplClass = (Class<? extends T>) ToolBeanDefinitionUtils.getBeanDefinitionClass(this.beanDefReg,
            ToolBeanDefinitionUtils.getBeanDefinitionOfType(this.beanFactory, this.beanDefReg, this.beanClass));

        if (this.beanImplClass == null) {
            throw new ToolBeanDataAccessException(String.format("Unable to find bean (class=%s) implementation class.", ToolClassUtils.getName(this.beanClass)));
        }
    }

    protected boolean containsBean(Session session, Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.getBeanById(session, beanIdValue) != null;
    }

    protected T getBeanById(Session session, Serializable beanIdValue) throws ToolBeanDataAccessException {
        try {
            return this.beanClass.cast(session.load(this.beanImplClass, beanIdValue));
        } catch (ObjectNotFoundException objectNotFoundException) {
            return null;
        }
    }

    protected List<T> getBeans(Session session, Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        // TODO: implement
        return new ArrayList<>();
    }

    protected T getBean(Session session, Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        // TODO: implement
        return null;
    }

    protected T setBean(Session session, T bean) throws ToolBeanDataAccessException {
        session.saveOrUpdate(bean);

        return bean;
    }

    protected T addBean(Session session, T bean) throws ToolBeanDataAccessException {
        session.save(bean);

        return bean;
    }

    protected T updateBean(Session session, T bean) throws ToolBeanDataAccessException {
        session.update(bean);

        return bean;
    }

    protected T removeBeanById(Session session, Serializable beanIdValue) throws ToolBeanDataAccessException {
        T bean = this.getBeanById(session, beanIdValue);

        return (bean != null) ? this.removeBean(session, bean) : null;
    }

    protected T removeBean(Session session, T bean) throws ToolBeanDataAccessException {
        session.delete(bean);

        return bean;
    }

    protected Session getCheckedSession() throws ToolBeanDataAccessException {
        Session session = this.getSession();

        if (session == null) {
            throw new ToolBeanDataAccessException(String.format("Bean (class=%s) Data Access Object (DAO) (class=%s) does not have a Hibernate session.",
                ToolClassUtils.getName(this.beanClass), ToolClassUtils.getName(this)));
        }

        return session;
    }

    protected Session getSession() {
        if (this.hasSessionFactory() && !this.sessionFactory.isClosed()) {
            try {
                return this.sessionFactory.getCurrentSession();
            } catch (HibernateException ignored) {
                return this.sessionFactory.openSession();
            }
        }

        return null;
    }

    protected boolean hasSessionFactory() {
        return this.sessionFactory != null;
    }

    @Override
    public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefReg) {
        this.beanDefReg = beanDefReg;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}
