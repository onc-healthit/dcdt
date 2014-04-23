package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanPostProcessor;
import org.apache.james.lifecycle.api.LogEnabled;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("toolLogEnabledBeanPostProc")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ToolLogEnabledBeanPostProcessor extends AbstractToolBeanPostProcessor<LogEnabled> {
    public ToolLogEnabledBeanPostProcessor() {
        super(LogEnabled.class, true, false);
    }

    @Override
    protected LogEnabled postProcessBeforeInitializationInternal(LogEnabled bean, String beanName) throws Exception {
        bean.setLog(LoggerFactory.getLogger(bean.getClass()));

        return super.postProcessBeforeInitializationInternal(bean, beanName);
    }
}
