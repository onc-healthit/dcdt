package gov.hhs.onc.dcdt.service.ldap.config.impl;

import org.apache.directory.server.config.ConfigurationElement;
import org.apache.directory.server.config.beans.PartitionBean;

public class AvlPartitionBean extends PartitionBean {
    @ConfigurationElement(attributeType = "ads-partitionCacheSize", isOptional = true, defaultValue = "-1")
    private int partitionCacheSize = -1;
    
    @ConfigurationElement(attributeType = "ads-partitionSyncOnWrite", isOptional = true, defaultValue = "true")
    private boolean partitionSyncOnWrite = true;

    public int getPartitionCacheSize() {
        return this.partitionCacheSize;
    }

    public void setPartitionCacheSize(int partitionCacheSize) {
        this.partitionCacheSize = partitionCacheSize;
    }

    public boolean isPartitionSyncOnWrite() {
        return this.partitionSyncOnWrite;
    }

    public void setPartitionSyncOnWrite(boolean partitionSyncOnWrite) {
        this.partitionSyncOnWrite = partitionSyncOnWrite;
    }
}
