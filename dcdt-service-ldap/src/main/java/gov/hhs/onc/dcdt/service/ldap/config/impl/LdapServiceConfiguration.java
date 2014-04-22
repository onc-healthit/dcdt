package gov.hhs.onc.dcdt.service.ldap.config.impl;

import gov.hhs.onc.dcdt.ldap.utils.ToolLdifUtils;
import gov.hhs.onc.dcdt.service.ldap.LdapServiceException;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.api.util.exception.Exceptions;
import org.apache.directory.server.config.beans.IndexBean;
import org.apache.directory.server.config.beans.LdapServerBean;
import org.apache.directory.server.config.beans.PartitionBean;
import org.apache.directory.server.config.builder.ServiceBuilder;
import org.apache.directory.server.constants.SystemSchemaConstants;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.InstanceLayout;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.api.schema.SchemaPartition;
import org.apache.directory.server.core.partition.impl.avl.AvlPartition;
import org.apache.directory.server.i18n.I18n;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.xdbm.Index;
import org.apache.directory.server.xdbm.impl.avl.AvlIndex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration("ldapServiceConfiguration")
public class LdapServiceConfiguration {
    @Bean(name = "serverLdap")
    @Lazy
    @Scope("prototype")
    public LdapServer createLdapServer(DirectoryService dirService, LdapServerBean ldapServerBean) throws Exception {
        return ServiceBuilder.createLdapServer(ldapServerBean, dirService);
    }

    @Bean(name = "dirService")
    @Lazy
    @Scope("prototype")
    public DirectoryService createDirectoryService(ToolDirectoryServiceBean dirServiceBean) throws Exception {
        SchemaManager schemaManager = dirServiceBean.getSchemaManager();
        InstanceLayout instanceLayout = dirServiceBean.getInstanceLayout();
        DirectoryService dirService = ServiceBuilder.createDirectoryService(dirServiceBean, instanceLayout, schemaManager);
        Partition partition;

        for (PartitionBean partitionBean : dirServiceBean.getPartitions()) {
            if (ToolClassUtils.isAssignable(partitionBean.getClass(), AvlPartitionBean.class)) {
                partition = createAvlPartitionInternal(schemaManager, ((AvlPartitionBean) partitionBean));

                if (partitionBean.getPartitionId().equals(SystemSchemaConstants.SCHEMA_NAME)) {
                    dirService.setSystemPartition(partition);
                } else {
                    dirService.addPartition(partition);
                }
            }
        }

        Partition schemaPartitionWrapped = createAvlPartitionInternal(schemaManager);
        SchemaPartition schemaPartition = new SchemaPartition(schemaManager);
        schemaPartition.setWrappedPartition(schemaPartitionWrapped);
        dirService.setSchemaPartition(schemaPartition);

        List<Throwable> schemaManagerErrors = schemaManager.getErrors();

        if (!schemaManagerErrors.isEmpty()) {
            throw new LdapServiceException(String.format(
                "Unable to create ApacheDS directory service (id=%s, class=%s) schemas (managerClass=%s, wrappedPartitionClass=%s): %s",
                dirService.getInstanceId(), ToolClassUtils.getName(dirService), ToolClassUtils.getName(schemaManager),
                ToolClassUtils.getName(schemaPartitionWrapped), I18n.err(I18n.ERR_317, Exceptions.printErrors(schemaManagerErrors))));
        }

        return dirService;
    }

    @Bean(name = "partitionAvl")
    @Lazy
    @Scope("prototype")
    public AvlPartition createAvlPartition(SchemaManager schemaManager, AvlPartitionBean avlPartitionBean) throws Exception {
        return createAvlPartitionInternal(schemaManager, avlPartitionBean);
    }

    private static AvlPartition createAvlPartitionInternal(SchemaManager schemaManager, AvlPartitionBean avlPartitionBean) throws Exception {
        AvlPartition avlPartition = createAvlPartitionInternal(schemaManager);
        avlPartition.setCacheSize(avlPartitionBean.getPartitionCacheSize());
        avlPartition.setId(avlPartitionBean.getPartitionId());
        avlPartition.setSyncOnWrite(avlPartitionBean.isPartitionSyncOnWrite());

        String contextEntry = avlPartitionBean.getContextEntry();

        if (!StringUtils.isBlank(contextEntry)) {
            avlPartition.setContextEntry(ToolLdifUtils.readEntry(StringUtils.replace(contextEntry, "\\\\n", "\n")).getEntry());
        }

        avlPartition.setIndexedAttributes(createAvlIndexes(avlPartitionBean.getIndexes()));

        Dn suffixDn = avlPartitionBean.getPartitionSuffix();

        if (suffixDn != null) {
            avlPartition.setSuffixDn(avlPartitionBean.getPartitionSuffix());
        }

        return avlPartition;
    }

    private static AvlPartition createAvlPartitionInternal(SchemaManager schemaManager) throws Exception {
        return new AvlPartition(schemaManager);
    }

    private static Set<Index<?, ?, String>> createAvlIndexes(List<IndexBean> indexBeans) throws Exception {
        Set<Index<?, ?, String>> indexes = new HashSet<>(indexBeans.size());

        for (IndexBean indexBean : indexBeans) {
            if (indexBean.isEnabled() && ToolClassUtils.isAssignable(indexBean.getClass(), AvlIndexBean.class)) {
                indexes.add(createAvlIndex(((AvlIndexBean) indexBean)));
            }
        }

        return indexes;
    }

    private static AvlIndex<?, ?> createAvlIndex(AvlIndexBean avlIndexBean) throws Exception {
        AvlIndex<String, Entry> avlIndex = new AvlIndex<>(avlIndexBean.getIndexAttributeId(), avlIndexBean.getIndexHasReverse());
        avlIndex.setCacheSize(avlIndexBean.getIndexCacheSize());

        return avlIndex;
    }
}
