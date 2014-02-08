package gov.hhs.onc.dcdt.data.dao.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessException;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.data.utils.ToolRestrictionsUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.format.support.FormattingConversionService;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanDao<T extends ToolBean> extends AbstractToolBean implements ToolBeanDao<T> {
    @Resource(name = "conversionService")
    protected FormattingConversionService convService;

    @Autowired
    protected SessionFactory sessionFactory;

    protected AbstractApplicationContext appContext;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractToolBeanDao(Class<T> beanClass, Class<? extends T> beanImplClass) {
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    public boolean containsBean() throws ToolBeanDataAccessException {
        return this.getBean() != null;
    }

    @Override
    public boolean containsBean(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.containsBean(this.getCheckedSession(), beanIdValue);
    }

    @Override
    public boolean containsBean(Criterion ... beanCriterions) throws ToolBeanDataAccessException {
        return this.containsBean(ToolArrayUtils.asList(beanCriterions));
    }

    @Override
    public boolean containsBean(Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException {
        return this.getBeanBy(beanCriterions) != null;
    }

    @Override
    public T getBean() throws ToolBeanDataAccessException {
        return this.getBeanBy(this.getCheckedSession(), null);
    }

    @Override
    public List<T> getBeans() throws ToolBeanDataAccessException {
        return this.getBeansBy(this.getCheckedSession(), null);
    }

    @Override
    public T getBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.getBeanById(this.getCheckedSession(), beanIdValue);
    }

    @Override
    public T getBeanBy(Criterion ... beanCriterions) throws ToolBeanDataAccessException {
        return this.getBeanBy(ToolArrayUtils.asList(beanCriterions));
    }

    @Override
    public T getBeanBy(Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException {
        return this.getBeanBy(this.getCheckedSession(), beanCriterions);
    }

    @Override
    public List<T> getBeansBy(Criterion ... beanCriterions) throws ToolBeanDataAccessException {
        return this.getBeansBy(ToolArrayUtils.asList(beanCriterions));
    }

    @Override
    public List<T> getBeansBy(Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException {
        return this.getBeansBy(this.getCheckedSession(), beanCriterions);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> loadBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.loadBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public List<T> loadBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansLoaded = new ArrayList<>();

        for (T bean : beans) {
            beansLoaded.add(this.loadBean(session, bean));
        }

        return beansLoaded;
    }

    @Override
    public T loadBean(T bean) throws ToolBeanDataAccessException {
        return this.loadBean(this.getCheckedSession(), bean);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> refreshBeans(T ... beans) throws ToolBeanDataAccessException {
        return this.refreshBeans(ToolArrayUtils.asList(beans));
    }

    @Override
    public List<T> refreshBeans(Iterable<T> beans) throws ToolBeanDataAccessException {
        Session session = this.getCheckedSession();
        List<T> beansRefreshed = new ArrayList<>();

        for (T bean : beans) {
            beansRefreshed.add(this.refreshBean(session, bean));
        }

        return beansRefreshed;
    }

    @Override
    public T refreshBean(T bean) throws ToolBeanDataAccessException {
        return this.refreshBean(this.getCheckedSession(), bean);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> setBeans(T ... beans) throws ToolBeanDataAccessException {
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
    @SuppressWarnings({ "unchecked" })
    public List<T> addBeans(T ... beans) throws ToolBeanDataAccessException {
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
    @SuppressWarnings({ "unchecked" })
    public List<T> updateBeans(T ... beans) throws ToolBeanDataAccessException {
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
    public T removeBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.removeBeanById(this.getCheckedSession(), beanIdValue);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> removeBeans(T ... beans) throws ToolBeanDataAccessException {
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

    protected boolean containsBean(Session session, @Nullable Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.getBeanById(session, beanIdValue) != null;
    }

    protected T getBeanById(Session session, @Nullable Serializable beanIdValue) throws ToolBeanDataAccessException {
        return (beanIdValue != null) ? this.getBeanBy(session, ToolArrayUtils.asList(Restrictions.idEq(beanIdValue))) : null;
    }

    protected T getBeanBy(Session session, @Nullable Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException {
        return ToolListUtils.getFirst(this.getBeansBy(this.buildBeanCriteria(beanCriterions).getExecutableCriteria(session).setMaxResults(1)));
    }

    protected List<T> getBeansBy(Session session, @Nullable Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException {
        return this.getBeansBy(this.buildBeanCriteria(beanCriterions).getExecutableCriteria(session));
    }

    @SuppressWarnings({ "unchecked" })
    protected List<T> getBeansBy(Criteria beanCriteria) throws ToolBeanDataAccessException {
        return (List<T>) beanCriteria.list();
    }

    @SuppressWarnings({ "unchecked" })
    protected T loadBean(Session session, T bean) throws ToolBeanDataAccessException {
        Serializable beanId = ToolBeanUtils.getId(ToolBeanUtils.wrap(bean, this.convService));

        if (this.containsBean(beanId)) {
            T beanPersistent = this.getBeanById(beanId);

            if (beanPersistent != null) {
                session.evict(beanPersistent);
            }

            session.load(bean, beanId);
        }

        return bean;
    }

    protected T refreshBean(Session session, T bean) throws ToolBeanDataAccessException {
        if (this.containsBean(session, ToolBeanUtils.getId(ToolBeanUtils.wrap(bean, this.convService)))) {
            session.refresh(bean);
        }

        return bean;
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

    @SuppressWarnings({ "unchecked" })
    protected T removeBean(Session session, T bean) throws ToolBeanDataAccessException {
        if (session.contains(bean)) {
            session.delete(bean);
        } else {
            T beanPersistent = this.getBeanById(ToolBeanUtils.getId(ToolBeanUtils.wrap(bean, this.convService)));

            if (beanPersistent != null) {
                session.delete(beanPersistent);
            }
        }

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

    protected synchronized Session getSession() {
        if (this.hasSessionFactory()) {
            try {
                return this.sessionFactory.getCurrentSession();
            } catch (HibernateException ignored) {
                return this.sessionFactory.openSession();
            }
        }

        return null;
    }

    protected synchronized boolean hasSessionFactory() {
        return (this.sessionFactory != null) && !this.sessionFactory.isClosed();
    }

    protected DetachedCriteria buildBeanCriteria(@Nullable Iterable<Criterion> beanCriterions) {
        return ToolRestrictionsUtils.addAll(DetachedCriteria.forClass(this.beanImplClass), beanCriterions);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
