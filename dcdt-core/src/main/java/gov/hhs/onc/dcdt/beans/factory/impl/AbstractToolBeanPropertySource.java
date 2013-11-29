package gov.hhs.onc.dcdt.beans.factory.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.beans.factory.ToolBeanPropertySource;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;
import java.beans.PropertyDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.env.PropertySource;

public abstract class AbstractToolBeanPropertySource<T extends ToolBean> extends AbstractToolBean implements ToolBeanPropertySource<T> {
    protected final static String PROP_NAME_PREFIX = "dcdt";

    protected String name;
    protected Class<T> sourceClass;
    protected T source;
    protected String propNamePrefix;

    protected AbstractToolBeanPropertySource(Class<T> sourceClass, String propNamePrefix) {
        this.sourceClass = sourceClass;
        this.propNamePrefix = propNamePrefix + ToolPropertyUtils.PROP_NAME_DELIM;
    }

    @Override
    public PropertySource<T> toPropertySource() throws ToolBeanException {
        if (!this.hasName() || !this.hasSource()) {
            return null;
        }

        return new PropertySource<T>(this.name, this.source) {
            @Override
            public Object getProperty(String propName) {
                if (hasSource() && propName.startsWith(propNamePrefix)) {
                    propName = StringUtils.removeStart(propName, propNamePrefix);

                    PropertyDescriptor sourcePropDesc = ToolBeanPropertyUtils.describeProperty(sourceClass, propName);

                    if (sourcePropDesc == null) {
                        throw new ToolBeanException("Unable to describe bean (class=" + ToolClassUtils.getName(sourceClass) + ") property (name=" + propName
                            + ").");
                    }

                    return ToolBeanPropertyUtils.read(sourcePropDesc, source, sourcePropDesc.getPropertyType());
                }

                return null;
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.name = this.beanName;
        this.source = ToolBeanFactoryUtils.getBeanOfType((ListableBeanFactory) this.beanFactory, this.sourceClass, true, false, false);
    }

    @Override
    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName() {
        this.name = name;
    }

    @Override
    public boolean hasSource() {
        return this.source != null;
    }

    @Override
    public T getSource() {
        return this.source;
    }

    @Override
    public void setSource(T source) {
        this.source = source;
    }
}
