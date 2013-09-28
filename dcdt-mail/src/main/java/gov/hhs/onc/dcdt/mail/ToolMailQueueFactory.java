package gov.hhs.onc.dcdt.mail;

import javax.management.InstanceAlreadyExistsException;
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
        } catch (Throwable th) {
            if (InstanceAlreadyExistsException.class.isAssignableFrom(th.getClass())) {
                LOGGER.error("Unable to register ActiveMQ mail queue managed bean (name=" + queueName + ").");
            }
        }
    }
}
