package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.config.ToolBeanPropertySources;
import java.util.List;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

public class ToolBeanPropertySourcesImpl extends MutablePropertySources implements ToolBeanPropertySources {
    public ToolBeanPropertySourcesImpl(List<PropertySource<? extends ToolBean>> beanPropSources) {
        super();

        for (PropertySource<? extends ToolBean> beanPropSource : beanPropSources) {
            this.addFirst(beanPropSource);
        }
    }
}
