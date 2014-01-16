package gov.hhs.onc.dcdt.data.dao.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.data.ToolBeanDataAccessException;
import gov.hhs.onc.dcdt.data.dao.ToolBeanDao;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanDao<T extends ToolBean> extends AbstractToolBean implements ToolBeanDao<T> {
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
    public T getFirstBean() throws ToolBeanDataAccessException {
        return this.getFirstBean(this.getCheckedSession());
    }

    @Override
    public List<T> getAllBeans() throws ToolBeanDataAccessException {
        return this.getAllBeans(this.getCheckedSession());
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
    public final List<T> loadBeans(T ... beans) throws ToolBeanDataAccessException {
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
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public final List<T> refreshBeans(T ... beans) throws ToolBeanDataAccessException {
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

    protected boolean containsBean(Session session, Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.getBeanById(session, beanIdValue) != null;
    }

    @SuppressWarnings({ "unchecked" })
    protected T getFirstBean(Session session) throws ToolBeanDataAccessException {
        return ToolListUtils.getFirst((List<T>) session.getNamedQuery(AbstractToolBean.QUERY_NAME_GET_ALL_BEANS).setMaxResults(1).list());
    }

    @SuppressWarnings({ "unchecked" })
    protected List<T> getAllBeans(Session session) throws ToolBeanDataAccessException {
        return (List<T>) session.getNamedQuery(AbstractToolBean.QUERY_NAME_GET_ALL_BEANS).list();
    }

    protected T getBeanById(Session session, Serializable beanIdValue) throws ToolBeanDataAccessException {
        return this.beanClass.cast(session.get(this.beanImplClass, beanIdValue));
    }

    protected List<T> getBeans(Session session, Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        // TODO: implement
        return new ArrayList<>();
    }

    protected T getBean(Session session, Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException {
        // TODO: implement
        return null;
    }

    @SuppressWarnings({ "unchecked" })
    protected T loadBean(Session session, T bean) throws ToolBeanDataAccessException {
        Serializable beanId = bean.getBeanId();

        if (this.containsBean(session, beanId)) {
            T beanPersistent;

            if ((beanPersistent = (T) session.get(bean.getClass(), beanId)) != null) {
                session.evict(beanPersistent);
            }

            session.load(bean, beanId);
        }

        return bean;
    }

    protected T refreshBean(Session session, T bean) throws ToolBeanDataAccessException {
        if (this.containsBean(session, bean.getBeanId())) {
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
            T beanPersistent;

            if ((beanPersistent = (T) session.get(bean.getClass(), bean.getBeanId())) != null) {
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

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
