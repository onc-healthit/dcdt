package gov.hhs.onc.dcdt.service.ldap.conf;

/**
 * @see org.apache.directory.server.core.annotations.ContextEntry
 */
public class ContextEntryConfig {
    private String entryLdif;

    public String getEntryLdif() {
        return this.entryLdif;
    }

    public void setEntryLdif(String entryLdif) {
        this.entryLdif = entryLdif;
    }
}
