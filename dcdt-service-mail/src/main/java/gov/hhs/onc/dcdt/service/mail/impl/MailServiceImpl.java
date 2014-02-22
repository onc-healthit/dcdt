package gov.hhs.onc.dcdt.service.mail.impl;

import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.mail.MailService;
import javax.annotation.Resource;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("mailServiceImpl")
@ServiceContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
public class MailServiceImpl extends AbstractToolService implements MailService {
    @Override
    @Resource(name = "taskExecServiceMail")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}