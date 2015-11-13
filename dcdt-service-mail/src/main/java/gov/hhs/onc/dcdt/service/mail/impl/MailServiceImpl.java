package gov.hhs.onc.dcdt.service.mail.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 4)
@ServiceContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
public class MailServiceImpl extends AbstractToolService<MailServerConfig, MailServer<MailServerConfig>> implements MailService {
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public MailServiceImpl() {
        super(MailServerConfig.class, ((Class<MailServer<MailServerConfig>>) ((Class<?>) MailServer.class)));
    }
}
