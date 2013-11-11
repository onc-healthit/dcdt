package gov.hhs.onc.dcdt.beans;


import gov.hhs.onc.dcdt.beans.factory.BeanDefinitionRegistryAware;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

public interface ToolBeanDao<T extends ToolBean> extends BeanDefinitionRegistryAware, BeanFactoryAware, InitializingBean {
    @SuppressWarnings({ "unchecked" })
    public boolean containsBeans(T ... beans) throws ToolBeanDataAccessException;

    public boolean containsBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public boolean containsBean(T bean) throws ToolBeanDataAccessException;

    public List<T> getBeansById(Serializable ... beanIdValues) throws ToolBeanDataAccessException;

    public List<T> getBeansById(Iterable<Serializable> beanIdValues) throws ToolBeanDataAccessException;

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

    @SuppressWarnings({ "unchecked" })
    public List<T> removeBeans(T ... beans) throws ToolBeanDataAccessException;

    public List<T> removeBeans(Iterable<T> beans) throws ToolBeanDataAccessException;

    public T removeBean(T bean) throws ToolBeanDataAccessException;

    public boolean hasSession();
}
