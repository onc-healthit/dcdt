package gov.hhs.onc.dcdt.data.impl;

import gov.hhs.onc.dcdt.convert.ToolConverter;
import gov.hhs.onc.dcdt.data.types.ToolUserType;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class ToolLocalSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {
    private AbstractApplicationContext appContext;
    private Set<ToolConverter> convs = new LinkedHashSet<>();

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder localSessionFactoryBuilder) {
        ToolUserType<?, ?, ?, ?> convUserType;

        for (ToolConverter conv : this.convs) {
            if (conv.hasUserTypeClass() && ((convUserType = ToolBeanFactoryUtils.getBeanOfType(this.appContext, conv.getUserTypeClass())) != null)) {
                localSessionFactoryBuilder.registerTypeOverride(convUserType, convUserType.getKeys());
            }
        }

        return super.buildSessionFactory(localSessionFactoryBuilder);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    public Set<ToolConverter> getConverters() {
        return this.convs;
    }

    public void setConverters(Set<ToolConverter> convs) {
        this.convs = convs;
    }
}
