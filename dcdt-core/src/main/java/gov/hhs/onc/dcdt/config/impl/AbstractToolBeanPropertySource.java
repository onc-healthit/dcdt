package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolBeanException;
import gov.hhs.onc.dcdt.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.beans.PropertyDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.PropertySource;

public abstract class AbstractToolBeanPropertySource<T extends ToolBean> extends PropertySource<T> {
    protected final static String PROP_NAME_PREFIX = "dcdt.";

    protected String propNamePrefix;

    protected AbstractToolBeanPropertySource(String name, T source, String propNamePrefix) {
        super(name, source);

        this.propNamePrefix = propNamePrefix;
    }

    @Override
    public Object getProperty(String propName) {
        if ((this.source != null) && propName.startsWith(this.propNamePrefix)) {
            propName = StringUtils.removeStart(propName, this.propNamePrefix);

            PropertyDescriptor sourcePropDesc = ToolBeanPropertyUtils.describeProperty(this.source.getClass(), propName, true, null);

            if (sourcePropDesc == null) {
                throw new ToolBeanException("Unable to describe bean (class=" + ToolClassUtils.getName(this.source) + ") property (name=" + propName + ").");
            }

            return ToolBeanPropertyUtils.read(sourcePropDesc, this.source, sourcePropDesc.getPropertyType());
        }

        return null;
    }
}
