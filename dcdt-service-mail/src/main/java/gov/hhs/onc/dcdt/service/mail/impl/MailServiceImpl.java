package gov.hhs.onc.dcdt.service.mail.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import javax.annotation.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 3)
@ServiceContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
public class MailServiceImpl extends AbstractToolService<MailServerConfig, MailServer<MailServerConfig>> implements MailService {
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public MailServiceImpl() {
        super(MailServerConfig.class, ((Class<MailServer<MailServerConfig>>) ((Class<?>) MailServer.class)));
    }

    @Override
    @Resource(name = "taskExecServiceMail")
    protected void setTaskExecutor(ThreadPoolTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
