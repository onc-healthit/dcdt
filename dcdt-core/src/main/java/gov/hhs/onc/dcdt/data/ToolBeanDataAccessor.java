package gov.hhs.onc.dcdt.data;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public interface ToolBeanDataAccessor<T extends ToolBean> {
    @SuppressWarnings({ "unchecked" })
    public boolean containsBeans(Serializable ... beanIdValues) throws ToolBeanDataAccessException;

    public boolean containsBeans(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException;

    public boolean containsBean(Serializable beanIdValue) throws ToolBeanDataAccessException;

    public T getFirstBean() throws ToolBeanDataAccessException;

    public List<T> getAllBeans() throws ToolBeanDataAccessException;

    public List<T> getBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException;

    public List<T> getBeansById(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException;

    public T getBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> getBeans(Pair<String, ? extends Serializable> ... beanColumnPairs) throws ToolBeanDataAccessException;

    public List<T> getBeans(Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public T getBean(Pair<String, ? extends Serializable> ... beanColumnPairs) throws ToolBeanDataAccessException;

    public T getBean(Iterable<Pair<String, ? extends Serializable>> beanColumnPairs) throws ToolBeanDataAccessException;

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

    public List<T> removeBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException;

    public List<T> removeBeansById(Iterable<? extends Serializable> beanIdValues) throws ToolBeanDataAccessException;

    public T removeBeanById(Serializable beanIdValue) throws ToolBeanDataAccessException;

    @SuppressWarnings({ "unchecked" })
    public List<T> removeBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> removeBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T removeBean(T bean) throws ToolBeanDataAccessException;
}
