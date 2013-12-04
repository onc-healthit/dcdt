package gov.hhs.onc.dcdt.service.ldap.conf;


import org.apache.directory.server.core.api.partition.Partition;

/**
 * @see org.apache.directory.server.core.annotations.CreatePartition
 */
public class PartitionConfig {
    private Class<? extends Partition> type = Partition.class;
    private String name;
    private String suffix;
    private ContextEntryConfig contextEntry = new ContextEntryConfig();
    private IndexConfig[] indexes;
    private int cacheSize = 1000;

    public int getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public ContextEntryConfig getContextEntry() {
        return this.contextEntry;
    }

    public void setContextEntry(ContextEntryConfig contextEntry) {
        this.contextEntry = contextEntry;
    }

    public IndexConfig[] getIndexes() {
        return this.indexes;
    }

    public void setIndexes(IndexConfig[] indexes) {
        this.indexes = indexes;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Class<? extends Partition> getType() {
        return this.type;
    }

    public void setType(Class<? extends Partition> type) {
        this.type = type;
    }
}
