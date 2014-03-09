package gov.hhs.onc.dcdt.service.ldap.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.ldap.LdapServiceException;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolDirectoryServiceBean;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolLdapServerBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.directory.server.config.beans.ServerBean;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.DirectoryBackedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("ldapServiceImpl")
@ServiceContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap-*.xml" })
public class LdapServiceImpl extends AbstractToolService implements LdapService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LdapServiceImpl.class);

    @Autowired(required = false)
    private List<ToolDirectoryServiceBean> dirServiceBeans;

    private Map<DirectoryService, List<DirectoryBackedService>> dirServiceServerMap;

    @Override
    protected void stopInternal() throws Exception {
        if (this.dirServiceServerMap != null) {
            for (DirectoryService dirService : this.dirServiceServerMap.keySet()) {
                if (dirService.isStarted()) {
                    for (DirectoryBackedService dirServiceServer : this.dirServiceServerMap.get(dirService)) {
                        if (dirServiceServer.isStarted()) {
                            try {
                                dirServiceServer.stop();
                            } catch (Exception e) {
                                LOGGER.error(
                                    String.format("Unable to stop ApacheDS directory service (id=%s, class=%s) server (id=%s, class=%s).",
                                        dirService.getInstanceId(), ToolClassUtils.getName(dirService), dirServiceServer.getServiceId(),
                                        ToolClassUtils.getName(dirServiceServer)), e);
                            }
                        }
                    }

                    try {
                        dirService.shutdown();
                    } catch (Exception e) {
                        LOGGER.error(
                            String.format("Unable to shutdown ApacheDS directory service (id=%s, class=%s).", dirService.getInstanceId(),
                                ToolClassUtils.getName(dirService)), e);
                    }
                }
            }
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    protected void startInternal() throws Exception {
        if (this.dirServiceBeans != null) {
            this.dirServiceServerMap = new LinkedHashMap<>(this.dirServiceBeans.size());

            DirectoryService dirService;
            List<ServerBean> dirServiceServerBeans;
            List<DirectoryBackedService> dirServiceServers;
            Class<? extends ServerBean> dirServiceServerBeanClass;
            DirectoryBackedService dirServiceServer;

            for (ToolDirectoryServiceBean dirServiceBean : this.dirServiceBeans) {
                dirService = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DirectoryService.class, dirServiceBean);

                try {
                    // noinspection ConstantConditions
                    dirService.startup();

                    LOGGER.debug(String.format("Started ApacheDS directory service (id=%s, class=%s).", dirService.getInstanceId(),
                        ToolClassUtils.getName(dirService)));
                } catch (Exception e) {
                    // noinspection ConstantConditions
                    throw new LdapServiceException(String.format("Unable to start ApacheDS directory service (id=%s, class=%s).", dirService.getInstanceId(),
                        ToolClassUtils.getName(dirService)), e);
                }

                dirServiceServerBeans = dirServiceBean.getServers();
                dirServiceServers = new ArrayList<>(dirServiceServerBeans.size());

                for (ServerBean dirServiceServerBean : dirServiceServerBeans) {
                    dirServiceServer = null;

                    if (ToolClassUtils.isAssignable((dirServiceServerBeanClass = dirServiceServerBean.getClass()), ToolLdapServerBean.class)) {
                        dirServiceServer = ToolBeanFactoryUtils.createBeanOfType(this.appContext, LdapServer.class, dirService, dirServiceServerBean);
                    }

                    if (dirServiceServer == null) {
                        throw new LdapServiceException(String.format("Unknown ApacheDS directory service (id=%s, class=%s) server bean type (class=%s).",
                            dirService.getInstanceId(), ToolClassUtils.getName(dirService), ToolClassUtils.getName(dirServiceServerBeanClass)));
                    }

                    try {
                        dirServiceServer.start();

                        LOGGER.debug(String.format("Started ApacheDS directory service (id=%s, class=%s) server (beanClass=%s, class=%s).",
                            dirService.getInstanceId(), ToolClassUtils.getName(dirService), ToolClassUtils.getName(dirServiceServerBeanClass),
                            ToolClassUtils.getName(dirServiceServer)));
                    } catch (Exception e) {
                        throw new LdapServiceException(String.format(
                            "Unable to start ApacheDS directory service (id=%s, class=%s) server (beanClass=%s, class=%s).", dirService.getInstanceId(),
                            ToolClassUtils.getName(dirService), ToolClassUtils.getName(dirServiceServerBeanClass), ToolClassUtils.getName(dirServiceServer)), e);
                    }

                    dirServiceServers.add(dirServiceServer);
                }

                this.dirServiceServerMap.put(dirService, dirServiceServers);
            }
        }
    }

    @Override
    @Resource(name = "taskExecServiceLdap")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
