package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.ToolRuntimeException;
import javax.management.InstanceAlreadyExistsException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.james.queue.activemq.ActiveMQMailQueueFactory;
import org.apache.james.queue.api.MailQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolMailQueueFactory extends ActiveMQMailQueueFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolMailQueueFactory.class);

    @Override
    protected synchronized void registerMBean(String queueName, MailQueue queue) {
        try {
            super.registerMBean(queueName, queue);
        } catch (Exception e) {
            if (ExceptionUtils.indexOfThrowable(e, InstanceAlreadyExistsException.class) == -1) {
                throw new ToolRuntimeException(String.format("Unable to register mail queue (name=%s) managed bean.", queueName), e);
            }

            LOGGER.error(String.format("Unable to register mail queue (name=%s) managed bean.", queueName), e);
        }
    }
}
