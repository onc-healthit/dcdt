package gov.hhs.onc.dcdt.service.ldap.conf;

import gov.hhs.onc.dcdt.service.ldap.factory.ToolDirectoryServiceFactory;
import java.io.File;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.core.factory.LdifPartitionFactory;
import org.apache.directory.server.core.factory.PartitionFactory;

/**
 * @see org.apache.directory.server.core.annotations.CreateDS
 */
public class DirectoryServiceConfig {
    private Class<? extends DirectoryServiceFactory> factoryClass = ToolDirectoryServiceFactory.class;
    private String name = "dirServiceDefault";
    private boolean enableAccessControl;
    private boolean allowAnonAccess = true;
    private boolean enableChangeLog;
    private PartitionConfig[] partitions;
    private Class<?>[] additionalInterceptors;
    private AuthenticatorConfig[] authenticators;
    private SchemaConfig[] loadedSchemas;
    private File instanceDir;
    private LdapServerConfig[] ldapServers;
    private File[] ldifFiles;
    private Class<? extends PartitionFactory> partitionFactoryClass = LdifPartitionFactory.class;

    public Class<?>[] getAdditionalInterceptors() {
        return this.additionalInterceptors;
    }

    public void setAdditionalInterceptors(Class<?>[] additionalInterceptors) {
        this.additionalInterceptors = additionalInterceptors;
    }

    public boolean isAllowAnonAccess() {
        return this.allowAnonAccess;
    }

    public void setAllowAnonAccess(boolean allowAnonAccess) {
        this.allowAnonAccess = allowAnonAccess;
    }

    public AuthenticatorConfig[] getAuthenticators() {
        return this.authenticators;
    }

    public void setAuthenticators(AuthenticatorConfig[] authenticators) {
        this.authenticators = authenticators;
    }

    public boolean isEnableAccessControl() {
        return this.enableAccessControl;
    }

    public void setEnableAccessControl(boolean enableAccessControl) {
        this.enableAccessControl = enableAccessControl;
    }

    public boolean isEnableChangeLog() {
        return this.enableChangeLog;
    }

    public void setEnableChangeLog(boolean enableChangeLog) {
        this.enableChangeLog = enableChangeLog;
    }

    public Class<? extends DirectoryServiceFactory> getFactoryClass() {
        return this.factoryClass;
    }

    public void setFactoryClass(Class<? extends DirectoryServiceFactory> factoryClass) {
        this.factoryClass = factoryClass;
    }

    public File getInstanceDir() {
        return this.instanceDir;
    }

    public void setInstanceDir(File instanceDir) {
        this.instanceDir = instanceDir;
    }

    public LdapServerConfig[] getLdapServers() {
        return this.ldapServers;
    }

    public void setLdapServers(LdapServerConfig[] ldapServers) {
        this.ldapServers = ldapServers;
    }

    public File[] getLdifFiles() {
        return this.ldifFiles;
    }

    public void setLdifFiles(File[] ldifFiles) {
        this.ldifFiles = ldifFiles;
    }

    public SchemaConfig[] getLoadedSchemas() {
        return this.loadedSchemas;
    }

    public void setLoadedSchemas(SchemaConfig[] loadedSchemas) {
        this.loadedSchemas = loadedSchemas;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends PartitionFactory> getPartitionFactoryClass() {
        return this.partitionFactoryClass;
    }

    public void setPartitionFactoryClass(Class<? extends PartitionFactory> partitionFactoryClass) {
        this.partitionFactoryClass = partitionFactoryClass;
    }

    public PartitionConfig[] getPartitions() {
        return this.partitions;
    }

    public void setPartitions(PartitionConfig[] partitions) {
        this.partitions = partitions;
    }
}
