package gov.hhs.onc.dcdt.service.ldap.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.ldap.LdapBindCredentialConfig;
import gov.hhs.onc.dcdt.ldap.ToolCoreSchemaConstants;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.ldap.LdapServiceException;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolDirectoryServiceBean;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseCredentialsExtractor;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolPredicateUtils.ToolPredicate;
import gov.hhs.onc.dcdt.utils.ToolTransformerUtils.ToolTransformer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
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
@ServiceContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap-*.xml" })
public class LdapServiceImpl extends AbstractToolService implements LdapService {
    private class DiscoveryTestcaseCredentialEntryTransformer extends ToolTransformer<DiscoveryTestcaseCredential, Entry> {
        private SchemaManager schemaManager;
        private Dn dataPartitionSuffix;

        public DiscoveryTestcaseCredentialEntryTransformer(SchemaManager schemaManager, Dn dataPartitionSuffix) {
            this.schemaManager = schemaManager;
            this.dataPartitionSuffix = dataPartitionSuffix;
        }

        @Override
        protected Entry transformInternal(DiscoveryTestcaseCredential discoveryTestcaseCred) throws Exception {
            String discoveryTestcaseCredName = discoveryTestcaseCred.getName();

            Entry discoveryTestcaseCredEntry =
                new DefaultEntry(this.schemaManager, this.dataPartitionSuffix.add(new Dn(new Rdn(SchemaConstants.CN_AT, discoveryTestcaseCredName))));
            discoveryTestcaseCredEntry.add(SchemaConstants.OBJECT_CLASS_AT, SchemaConstants.TOP_OC, SchemaConstants.PERSON_OC,
                SchemaConstants.ORGANIZATIONAL_PERSON_OC, SchemaConstants.INET_ORG_PERSON_OC);
            discoveryTestcaseCredEntry.add(SchemaConstants.CN_AT, discoveryTestcaseCredName);
            // noinspection ConstantConditions
            discoveryTestcaseCredEntry.add(ToolCoreSchemaConstants.ATTR_TYPE_NAME_MAIL, discoveryTestcaseCred.getLocation().getMailAddress().toAddress());
            discoveryTestcaseCredEntry.add(SchemaConstants.SN_AT, discoveryTestcaseCredName);
            // noinspection ConstantConditions
            discoveryTestcaseCredEntry.add(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT,
                CertificateUtils.writeCertificate(discoveryTestcaseCred.getCredentialInfo().getCertificateDescriptor().getCertificate(), DataEncoding.DER));

            return discoveryTestcaseCredEntry;
        }
    }

    private class DiscoveryTestcaseCredentialEntryPredicate extends ToolPredicate<DiscoveryTestcaseCredential> {
        private String dataPartitionId;

        public DiscoveryTestcaseCredentialEntryPredicate(String dataPartitionId) {
            this.dataPartitionId = dataPartitionId;
        }

        @Override
        protected boolean evaluateInternal(@Nullable DiscoveryTestcaseCredential discoveryTestcaseCred) {
            DiscoveryTestcaseCredentialLocation discoveryTestcaseCredLoc;
            CredentialInfo discoveryTestcaseCredInfo;

            // noinspection ConstantConditions
            return discoveryTestcaseCred.hasBindingType() && discoveryTestcaseCred.getBindingType().isBound() && discoveryTestcaseCred.hasLocation()
                && (discoveryTestcaseCredLoc = discoveryTestcaseCred.getLocation()).getType().isLdap() && discoveryTestcaseCredLoc.hasLdapConfig()
                && discoveryTestcaseCredLoc.getLdapConfig().getPartitionId().equals(this.dataPartitionId) && discoveryTestcaseCredLoc.hasMailAddress()
                && discoveryTestcaseCred.hasCredentialInfo()
                && (discoveryTestcaseCredInfo = discoveryTestcaseCred.getCredentialInfo()).hasCertificateDescriptor()
                && discoveryTestcaseCredInfo.getCertificateDescriptor().hasCertificate();
        }
    }

    private final static int LEN_LDAP_BIND_ADMIN_PASS_GEN = 5;

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
        String dataPartitionId = ldapConfig.getPartitionId();
        Dn dataPartitionSuffix = ldapConfig.getPartitionSuffix();
        Entry dataPartitionContextEntry = ldapConfig.getPartitionContextEntry();
        LdapBindCredentialConfig ldapBindCredConfigAdmin = dirServiceBean.getLdapConfig().getBindCredentialConfigAdmin();
        Dn ldapBindAdminDn = ldapBindCredConfigAdmin.getBindDn();
        String ldapBindAdminPass = ldapBindCredConfigAdmin.getBindPassword();
        // noinspection ConstantConditions
        CoreSession adminSession = dirService.getAdminSession();

        if (StringUtils.isBlank(ldapBindAdminPass)) {
            ldapBindAdminPass = RandomStringUtils.randomAscii(LEN_LDAP_BIND_ADMIN_PASS_GEN);
        }

        try {
            adminSession.modify(ldapBindAdminDn, new DefaultModification(ModificationOperation.REPLACE_ATTRIBUTE, SchemaConstants.USER_PASSWORD_AT,
                ldapBindAdminPass));

            LOGGER.debug(String.format("Modified ApacheDS directory service (id=%s, class=%s) admin entry (dn={%s}) password: %s", dirService.getInstanceId(),
                ToolClassUtils.getName(dirService), ldapBindAdminDn, ldapBindAdminPass));
        } catch (LdapException e) {
            throw new LdapServiceException(String.format("Unable to modify ApacheDS directory service (id=%s, class=%s) admin entry (dn={%s}) password.",
                dirService.getInstanceId(), ToolClassUtils.getName(dirService), ldapBindAdminDn), e);
        }

        Collection<Entry> dataEntries =
            CollectionUtils.collect(CollectionUtils.select(
                IteratorUtils.asIterable(ToolIteratorUtils.chainedIterator(CollectionUtils.collect(
                    ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class), DiscoveryTestcaseCredentialsExtractor.INSTANCE))),
                new DiscoveryTestcaseCredentialEntryPredicate(dataPartitionId)), new DiscoveryTestcaseCredentialEntryTransformer(schemaManager,
                dataPartitionSuffix));

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

        if (dataEntries != null) {
            for (Entry dataEntry : dataEntries) {
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
    @Resource(name = "taskExecServiceLdap")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
