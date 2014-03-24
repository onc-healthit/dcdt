package gov.hhs.onc.dcdt.data.dto.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.data.dto.ToolBeanDto;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanDto<T extends ToolBean> extends AbstractToolBean implements ToolBeanDto<T> {
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractToolBeanDto(Class<T> beanClass, Class<? extends T> beanImplClass) {
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Class<? extends T> getBeanImplClass() {
        return this.beanImplClass;
    }
}
