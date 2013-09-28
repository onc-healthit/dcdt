package gov.hhs.onc.dcdt.ldap.conf;

/**
 * @see org.apache.directory.server.core.annotations.LoadSchema
 */
public class SchemaConfig {
    private String name;
    private boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
