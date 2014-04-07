package gov.hhs.onc.dcdt.data;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.io.Serializable;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface ToolBeanDataAccessor<T extends ToolBean> extends ToolBean {
    public boolean containsBean() throws ToolBeanDataAccessException;

    public boolean containsBeanOfId(Serializable beanIdValue) throws ToolBeanDataAccessException;

    public boolean containsBean(Criterion ... beanCriterions) throws ToolBeanDataAccessException;

    public boolean containsBean(Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException;

    public T getBean() throws ToolBeanDataAccessException;

    public List<T> getBeans() throws ToolBeanDataAccessException;

    public T getBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException;

    public T getBeanBy(Criterion ... beanCriterions) throws ToolBeanDataAccessException;

    public T getBeanBy(Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException;

    public List<T> getBeansBy(Criterion ... beanCriterions) throws ToolBeanDataAccessException;

    public List<T> getBeansBy(Iterable<Criterion> beanCriterions) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> loadBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> loadBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T loadBean(T bean) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> refreshBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> refreshBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T refreshBean(T bean) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> setBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> setBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T setBean(T bean) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> addBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> addBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T addBean(T bean) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> updateBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> updateBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T updateBean(T bean) throws ToolBeanDataAccessException;

    public T removeBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> removeBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> removeBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T removeBean(T bean) throws ToolBeanDataAccessException;
}
