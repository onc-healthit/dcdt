package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.ToolRuntimeException;
import javax.management.InstanceAlreadyExistsException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.james.queue.activemq.ActiveMQMailQueueFactory;
import org.apache.james.queue.api.MailQueue;

public class ToolMailQueueFactory extends ActiveMQMailQueueFactory {
    @Override
    protected synchronized void registerMBean(String queueName, MailQueue queue) {
        try {
            super.registerMBean(queueName, queue);
        } catch (Throwable th) {
            if (ExceptionUtils.indexOfThrowable(th, InstanceAlreadyExistsException.class) == -1) {
                throw new ToolRuntimeException(String.format("Unable to register mail queue (name=%s) managed bean.", queueName), th);
            }
        }
    }
}
