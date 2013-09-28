package gov.hhs.onc.dcdt.ldap.conf;


import org.apache.directory.server.xdbm.Index;

/**
 * @see org.apache.directory.server.core.annotations.CreateIndex
 */
public class IndexConfig {
    private Class<? extends Index> type = Index.class;
    private int cacheSize = 1000;
    private String attribute;

    public String getAttribute() {
        return this.attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public Class<? extends Index> getType() {
        return this.type;
    }

    public void setType(Class<? extends Index> type) {
        this.type = type;
    }
}
