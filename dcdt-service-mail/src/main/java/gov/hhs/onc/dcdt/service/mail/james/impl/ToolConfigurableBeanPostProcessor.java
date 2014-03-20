package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import org.apache.james.container.spring.lifecycle.ConfigurableBeanPostProcessor;
import org.apache.james.lifecycle.api.Configurable;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class ToolConfigurableBeanPostProcessor extends ConfigurableBeanPostProcessor implements ApplicationContextAware {
    private final static String BEAN_META_KEY_CONFIGURABLE_BEAN_NAME = "configurableBeanName";

    private AbstractApplicationContext appContext;

    @Override
    protected void executeLifecycleMethodBeforeInit(Configurable bean, String beanName) throws Exception {
        BeanMetadataAttribute configurableBeanNameMetaAttr =
            ((AbstractBeanDefinition) this.appContext.getBeanFactory().getBeanDefinition(beanName)).getMetadataAttribute(BEAN_META_KEY_CONFIGURABLE_BEAN_NAME);
        String configurableBeanName;

        super.executeLifecycleMethodBeforeInit(
            bean,
            (((configurableBeanNameMetaAttr != null) && ToolBeanFactoryUtils.isBeanAlias(this.appContext, (configurableBeanName =
                ((String) configurableBeanNameMetaAttr.getValue())))) ? configurableBeanName : beanName));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
