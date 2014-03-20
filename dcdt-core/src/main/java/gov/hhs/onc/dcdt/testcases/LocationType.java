package gov.hhs.onc.dcdt.testcases;

public enum LocationType {
    DNS("dns"), LDAP("ldap");

    private final String loc;

    private LocationType(String loc) {
        this.loc = loc;
    }

    public String getLocation() {
        return this.loc;
    }

    public boolean isDns() {
        return this == DNS;
    }

    public boolean isLdap() {
        return this == LDAP;
    }
}
