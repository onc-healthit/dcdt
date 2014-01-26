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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Proxy;

@Access(AccessType.PROPERTY)
@MappedSuperclass
@Proxy(lazy = false)
public abstract class AbstractToolBean implements ToolBean {
    protected transient String beanName;

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
    @SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass" })
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
