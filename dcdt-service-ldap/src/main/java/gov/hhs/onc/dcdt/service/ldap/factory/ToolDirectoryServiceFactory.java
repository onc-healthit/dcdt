package gov.hhs.onc.dcdt.service.ldap.factory;


import gov.hhs.onc.dcdt.service.ldap.conf.DirectoryServiceConfig;
import gov.hhs.onc.dcdt.service.ldap.conf.PartitionConfig;
import gov.hhs.onc.dcdt.service.ldap.conf.SchemaConfig;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.schema.LdapComparator;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.api.ldap.model.schema.comparators.NormalizingComparator;
import org.apache.directory.api.ldap.schemaextractor.SchemaLdifExtractor;
import org.apache.directory.api.ldap.schemaextractor.impl.DefaultSchemaLdifExtractor;
import org.apache.directory.api.ldap.schemaloader.LdifSchemaLoader;
import org.apache.directory.api.ldap.schemamanager.impl.DefaultSchemaManager;
import org.apache.directory.api.util.exception.Exceptions;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.api.CacheService;
import org.apache.directory.server.core.api.CoreSession;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.InstanceLayout;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.api.schema.SchemaPartition;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.core.factory.PartitionFactory;
import org.apache.directory.server.core.partition.ldif.LdifPartition;
import org.apache.directory.server.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolDirServiceFactory")
@Scope("singleton")
public class ToolDirectoryServiceFactory implements DirectoryServiceFactory {
    @Autowired
    private DirectoryServiceConfig dirServiceConfig;

    private DirectoryService dirService;
    private PartitionFactory partitionFactory;

    @Override
    public void init(String name) throws Exception {
        if ((this.dirService != null) || (this.partitionFactory != null)) {
            return;
        }

        this.dirService = new DefaultDirectoryService();
        this.dirService.setInstanceId(name);

        this.partitionFactory = this.dirServiceConfig.getPartitionFactoryClass().newInstance();

        this.initInstanceDirectory();
        this.initCacheService();
        this.initSchemas();
        this.initPartitions();

        this.dirService.startup();

        this.loadLdifFiles();
    }

    @Override
    public DirectoryService getDirectoryService() throws Exception {
        return this.dirService;
    }

    @Override
    public PartitionFactory getPartitionFactory() throws Exception {
        return this.partitionFactory;
    }

    private void loadLdifFiles() throws Exception {
        File[] ldifFiles = this.dirServiceConfig.getLdifFiles();

        if (ldifFiles != null) {
            CoreSession adminSession = this.dirService.getAdminSession();
            SchemaManager schemaManager = this.dirService.getSchemaManager();

            for (File ldifFile : ldifFiles) {
                for (LdifEntry ldifEntry : new LdifReader(ldifFile)) {
                    if (ldifEntry.isChangeAdd() || ldifEntry.isLdifContent()) {
                        adminSession.add(new DefaultEntry(schemaManager, ldifEntry.getEntry()));
                    } else if (ldifEntry.isChangeModify()) {
                        adminSession.modify(ldifEntry.getDn(), ldifEntry.getModifications());
                    } else {
                        throw new LdapException(I18n.err(I18n.ERR_117, ldifEntry.getChangeType()));
                    }
                }

            }
        }
    }

    private void initSystemPartition() throws Exception {
        SchemaManager schemaManager = this.dirService.getSchemaManager();

        Partition sysPartition = this.partitionFactory.createPartition(schemaManager, "system", ServerDNConstants.SYSTEM_DN, 500, new File(this.dirService
            .getInstanceLayout().getPartitionsDirectory(), "system"));
        sysPartition.setSchemaManager(schemaManager);

        this.partitionFactory.addIndex(sysPartition, SchemaConstants.OBJECT_CLASS_AT, 100);

        this.dirService.setSystemPartition(sysPartition);
    }

    private void initPartitions() throws Exception {
        this.initSystemPartition();

        SchemaManager schemaManager = this.dirService.getSchemaManager();
        File partitionsDir = this.dirService.getInstanceLayout().getPartitionsDirectory();
        PartitionConfig[] partitionConfigs = this.dirServiceConfig.getPartitions();
        String partitionName;
        Partition partition;

        if (partitionConfigs != null) {
            for (PartitionConfig partitionConfig : partitionConfigs) {
                partitionName = partitionConfig.getName();
                partition = this.partitionFactory.createPartition(schemaManager, partitionName, partitionConfig.getSuffix(), partitionConfig.getCacheSize(),
                    new File(partitionsDir, partitionName));

                this.dirService.addPartition(partition);
            }
        }
    }

    private void initSystemSchemas() throws Exception {
        File partitionsDir = this.dirService.getInstanceLayout().getPartitionsDirectory(), schemasDir = new File(partitionsDir, "schema");

        SchemaLdifExtractor schemaLdifExtractor = new DefaultSchemaLdifExtractor(partitionsDir);
        schemaLdifExtractor.extractOrCopy();

        SchemaManager schemaManager = new DefaultSchemaManager(new LdifSchemaLoader(schemasDir));
        schemaManager.loadAllEnabled();

        for (LdapComparator<?> ldapComparator : schemaManager.getComparatorRegistry()) {
            if (NormalizingComparator.class.isAssignableFrom(ldapComparator.getClass())) {
                ((NormalizingComparator) ldapComparator).setOnServer();
            }
        }

        this.dirService.setSchemaManager(schemaManager);

        LdifPartition schemasLdifPartition = new LdifPartition(schemaManager);
        schemasLdifPartition.setPartitionPath(schemasDir.toURI());

        SchemaPartition schemasPartition = new SchemaPartition(schemaManager);
        schemasPartition.setWrappedPartition(schemasLdifPartition);
        this.dirService.setSchemaPartition(schemasPartition);
    }

    private void initSchemas() throws Exception {
        this.initSystemSchemas();

        SchemaManager schemaManager = this.dirService.getSchemaManager();
        SchemaConfig[] schemaConfigs = this.dirServiceConfig.getLoadedSchemas();

        if (schemaConfigs != null) {
            for (SchemaConfig schemaConfig : schemaConfigs) {
                if (schemaConfig.isEnabled()) {
                    schemaManager.load(schemaConfig.getName());
                }
            }
        }

        // TODO: improve error handling
        List<Throwable> schemasErrors = schemaManager.getErrors();

        if (!schemasErrors.isEmpty()) {
            throw new LdapException(I18n.err(I18n.ERR_317, Exceptions.printErrors(schemasErrors)));
        }
    }

    private void initCacheService() throws Exception {
        CacheService cacheService = new CacheService();
        cacheService.initialize(this.dirService.getInstanceLayout());

        this.dirService.setCacheService(cacheService);
    }

    private void initInstanceDirectory() throws Exception {
        File instanceDir = this.dirServiceConfig.getInstanceDir();

        // TODO: improve instance directory validation
        if (instanceDir.exists() && instanceDir.isDirectory()) {
            FileUtils.deleteDirectory(instanceDir);
        }

        this.dirService.setInstanceLayout(new InstanceLayout(instanceDir));
    }
}
