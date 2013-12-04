package gov.hhs.onc.dcdt.testcases.hosting;

public enum HostingTestcaseLocation {
    DNS("dns"), LDAP("ldap");

    private final String location;

    private HostingTestcaseLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }
}
