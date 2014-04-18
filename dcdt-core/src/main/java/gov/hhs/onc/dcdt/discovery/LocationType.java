package gov.hhs.onc.dcdt.discovery;

public enum LocationType {
    DNS, LDAP;

    public boolean isDns() {
        return (this == DNS);
    }

    public boolean isLdap() {
        return (this == LDAP);
    }
}
