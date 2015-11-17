package gov.hhs.onc.dcdt.service.ldap.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.ldap.LdapTransportProtocol;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.ldap.config.LdapServerConfig;
import gov.hhs.onc.dcdt.service.ldap.server.LdapServer;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("ldapServiceImpl")
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 3)
@ServiceContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap-*.xml" })
public class LdapServiceImpl extends AbstractToolService<LdapTransportProtocol, LdapServerConfig, LdapServer> implements LdapService {
    public LdapServiceImpl() {
        super(LdapServer.class);
    }

    @Autowired(required = false)
    @Override
    public void setServers(List<LdapServer> servers) {
        super.setServers(servers);
    }

    @Override
    @Resource(name = "taskExecServiceLdap")
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
