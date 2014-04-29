package gov.hhs.onc.dcdt.service.ldap.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceLdapConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceLdapCredentialConfig;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.ldap.LdapServiceException;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolDirectoryServiceBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.config.beans.LdapServerBean;
import org.apache.directory.server.config.beans.ServerBean;
import org.apache.directory.server.core.api.CoreSession;
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
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 1)
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

            for (ToolDirectoryServiceBean dirServiceBean : this.dirServiceBeans) {
                dirService = this.buildDirectoryService(dirServiceBean);
                dirServiceServerBeans = dirServiceBean.getServers();
                dirServiceServers = new ArrayList<>(dirServiceServerBeans.size());

                for (ServerBean dirServiceServerBean : dirServiceServerBeans) {
                    dirServiceServers.add(this.buildDirectoryServiceServer(dirService, dirServiceServerBean));
                }

                this.dirServiceServerMap.put(dirService, dirServiceServers);
            }
        }
    }

    protected DirectoryBackedService buildDirectoryServiceServer(DirectoryService dirService, ServerBean dirServiceServerBean) throws LdapServiceException {
        Class<? extends ServerBean> dirServiceServerBeanClass = dirServiceServerBean.getClass();
        DirectoryBackedService dirServiceServer = null;

        if (ToolClassUtils.isAssignable(dirServiceServerBeanClass, LdapServerBean.class)) {
            dirServiceServer = ToolBeanFactoryUtils.createBeanOfType(this.appContext, LdapServer.class, dirService, dirServiceServerBean);
        }

        if (dirServiceServer == null) {
            throw new LdapServiceException(String.format("Unknown ApacheDS directory service (id=%s, class=%s) server bean type (class=%s).",
                dirService.getInstanceId(), ToolClassUtils.getName(dirService), ToolClassUtils.getName(dirServiceServerBeanClass)));
        }

        try {
            dirServiceServer.start();

            LOGGER.debug(String.format("Started ApacheDS directory service (id=%s, class=%s) server (beanClass=%s, class=%s).", dirService.getInstanceId(),
                ToolClassUtils.getName(dirService), ToolClassUtils.getName(dirServiceServerBeanClass), ToolClassUtils.getName(dirServiceServer)));
        } catch (Exception e) {
            throw new LdapServiceException(String.format("Unable to start ApacheDS directory service (id=%s, class=%s) server (beanClass=%s, class=%s).",
                dirService.getInstanceId(), ToolClassUtils.getName(dirService), ToolClassUtils.getName(dirServiceServerBeanClass),
                ToolClassUtils.getName(dirServiceServer)), e);
        }

        return dirServiceServer;
    }

    protected DirectoryService buildDirectoryService(ToolDirectoryServiceBean dirServiceBean) throws LdapServiceException {
        DirectoryService dirService = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DirectoryService.class, dirServiceBean);

        CryptographyUtils.initializeProvider();

        try {
            // noinspection ConstantConditions
            dirService.startup();

            LOGGER
                .debug(String.format("Started ApacheDS directory service (id=%s, class=%s).", dirService.getInstanceId(), ToolClassUtils.getName(dirService)));
        } catch (Exception e) {
            // noinspection ConstantConditions
            throw new LdapServiceException(String.format("Unable to start ApacheDS directory service (id=%s, class=%s).", dirService.getInstanceId(),
                ToolClassUtils.getName(dirService)), e);
        }

        SchemaManager schemaManager = dirService.getSchemaManager();
        InstanceLdapConfig ldapConfig = dirServiceBean.getLdapConfig();
        String dataPartitionId = ldapConfig.getDataPartitionId();
        Dn dataPartitionSuffix = ldapConfig.getDataPartitionSuffix();
        Entry dataPartitionContextEntry = ldapConfig.getDataPartitionContextEntry();
        InstanceLdapCredentialConfig ldapCredConfigAdmin = dirServiceBean.getLdapConfig().getCredentialConfigAdmin();
        Dn ldapAdminDn = ldapCredConfigAdmin.getId();
        String ldapAdminPass = ldapCredConfigAdmin.getSecret();
        // noinspection ConstantConditions
        CoreSession adminSession = dirService.getAdminSession();

        try {
            adminSession.modify(ldapAdminDn, new DefaultModification(ModificationOperation.REPLACE_ATTRIBUTE, SchemaConstants.USER_PASSWORD_AT, ldapAdminPass));

            LOGGER.debug(String.format("Modified ApacheDS directory service (id=%s, class=%s) admin entry (dn={%s}) password: %s", dirService.getInstanceId(),
                ToolClassUtils.getName(dirService), ldapAdminDn, ldapAdminPass));
        } catch (LdapException e) {
            throw new LdapServiceException(String.format("Unable to modify ApacheDS directory service (id=%s, class=%s) admin entry (dn={%s}) password.",
                dirService.getInstanceId(), ToolClassUtils.getName(dirService), ldapAdminPass), e);
        }

        try {
            adminSession.add(new DefaultEntry(schemaManager, dataPartitionContextEntry));

            // noinspection ConstantConditions
            LOGGER.debug(String.format("Added data LDAP partition (id=%s, suffix={%s}) context entry:\n%s", dataPartitionId, dataPartitionSuffix,
                dataPartitionContextEntry));
        } catch (LdapException e) {
            // noinspection ConstantConditions
            throw new LdapServiceException(String.format("Unable to add data LDAP partition (id=%s, suffix={%s}) context entry:\n%s", dataPartitionId,
                dataPartitionSuffix, dataPartitionContextEntry), e);
        }

        if (dirServiceBean.hasDataEntries()) {
            // noinspection ConstantConditions
            for (Entry dataEntry : dirServiceBean.getDataEntries()) {
                try {
                    adminSession.add(dataEntry);

                    // noinspection ConstantConditions
                    LOGGER.debug(String.format("Added data LDAP entry (dn={%s}) to data LDAP partition (id=%s, suffix={%s}):\n%s", dataEntry.getDn(),
                        dataPartitionId, dataPartitionSuffix, dataEntry));
                } catch (LdapException e) {
                    // noinspection ConstantConditions
                    throw new LdapServiceException(String.format("Unable to add data LDAP entry (dn={%s}) to data LDAP partition (id=%s, suffix={%s}):\n%s",
                        dataEntry.getDn(), dataPartitionId, dataPartitionSuffix, dataEntry), e);
                }
            }
        }

        return dirService;
    }

    @Override
    public boolean hasDirectoryServiceBeans() {
        return !CollectionUtils.isEmpty(this.dirServiceBeans);
    }

    @Nullable
    @Override
    public List<ToolDirectoryServiceBean> getDirectoryServiceBeans() {
        return this.dirServiceBeans;
    }

    @Override
    @Resource(name = "taskExecServiceLdap")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
