package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.utils.ToolBeanPropertyUtils;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.annotations.Proxy;

@Access(AccessType.PROPERTY)
@MappedSuperclass
@Proxy(lazy = false)
public abstract class AbstractToolBean implements ToolBean {
    protected String beanName;

    @Override
    public boolean hasBeanId() {
        return this.getBeanId() != null;
    }

    @Nullable
    @Override
    @Transient
    public Serializable getBeanId() {
        for (PropertyDescriptor beanPropDesc : ToolBeanPropertyUtils.describeProperties(this.getClass())) {
            if (ToolBeanPropertyUtils.isReadable(beanPropDesc) && beanPropDesc.getReadMethod().isAnnotationPresent(Id.class)) {
                return ToolBeanPropertyUtils.read(beanPropDesc, this, Serializable.class);
            }
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    @Transient
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
