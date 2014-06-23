package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.util.Locale;
import javax.annotation.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component("formatterNamedBean")
public class NamedBeanFormatter extends AbstractToolFormatter<ToolNamedBean> implements ApplicationContextAware {
    private AbstractApplicationContext appContext;

    public NamedBeanFormatter() {
        super(ToolNamedBean.class);
    }

    @Nullable
    @Override
    protected String printInternal(ToolNamedBean obj, Locale locale) throws Exception {
        return obj.getName();
    }

    @Nullable
    @Override
    protected ToolNamedBean parseInternal(String str, Locale locale) throws Exception {
        return ToolBeanUtils.findNamed(this.appContext, str);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
