package gov.hhs.onc.dcdt.service.ldap.config.impl;

import org.apache.directory.server.config.ConfigurationElement;
import org.apache.directory.server.config.beans.IndexBean;

public class AvlIndexBean extends IndexBean {
    private final int CACHE_SIZE_DEFAULT = 100;

    @ConfigurationElement(attributeType = "ads-indexCacheSize", isOptional = true)
    private int indexCacheSize = CACHE_SIZE_DEFAULT;

    public int getIndexCacheSize() {
        return this.indexCacheSize;
    }

    public void setIndexCacheSize(int indexCacheSize) {
        this.indexCacheSize = indexCacheSize;
    }
}
