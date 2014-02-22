package gov.hhs.onc.dcdt.service.ldap.impl;

import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import javax.annotation.Resource;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("ldapServiceImpl")
@ServiceContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap-*.xml" })
public class LdapServiceImpl extends AbstractToolService implements LdapService {
    @Override
    @Resource(name = "taskExecServiceLdap")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
