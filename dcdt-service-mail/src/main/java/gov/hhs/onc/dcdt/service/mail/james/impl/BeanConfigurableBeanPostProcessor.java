package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanPostProcessor;
import gov.hhs.onc.dcdt.config.utils.ToolConfigurationUtils;
import gov.hhs.onc.dcdt.service.mail.MailServiceException;
import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component("beanConfigurableBeanPostProc")
@SuppressWarnings({ "rawtypes" })
public class BeanConfigurableBeanPostProcessor extends AbstractToolBeanPostProcessor<BeanConfigurable> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BeanConfigurableBeanPostProcessor.class);

    @Autowired
    private ConversionService convService;

    public BeanConfigurableBeanPostProcessor() {
        super(BeanConfigurable.class, true, false);
    }

    @Override
    protected BeanConfigurable postProcessBeforeInitializationInternal(BeanConfigurable bean, String beanName) throws Exception {
        HierarchicalConfiguration beanConfig = ToolConfigurationUtils.build(this.convService, bean.getConfigBean());

        try {
            bean.configure(beanConfig);

            LOGGER.trace(String.format("Configured (class=%s) James bean (name=%s, class=%s):\n%s", ToolClassUtils.getName(this), beanName,
                ToolClassUtils.getName(bean), ToolConfigurationUtils.writeXml(beanConfig)));
        } catch (ConfigurationException e) {
            throw new MailServiceException(String.format("Unable to configure (class=%s) James bean (name=%s, class=%s):\n%s", ToolClassUtils.getName(this),
                beanName, ToolClassUtils.getName(bean), ToolConfigurationUtils.writeXml(beanConfig)), e);
        }

        return super.postProcessBeforeInitializationInternal(bean, beanName);
    }
}
