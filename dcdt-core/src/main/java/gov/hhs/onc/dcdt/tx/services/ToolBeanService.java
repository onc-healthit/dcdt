package gov.hhs.onc.dcdt.tx.services;

import gov.hhs.onc.dcdt.beans.ToolBean;

import java.io.Serializable;
import java.util.List;

public interface ToolBeanService<T extends ToolBean> {

    public List<T> getBeansById(Iterable<? extends Serializable> beanIdValues);

    public boolean containsBeans(Iterable<? extends Serializable> beanIdValues);

    public List<T> addBeans(Iterable<T> beans);

    public List<T> updateBeans(Iterable<T> beans);

    public List<T> removeBeans(Iterable<T> beans);

}
