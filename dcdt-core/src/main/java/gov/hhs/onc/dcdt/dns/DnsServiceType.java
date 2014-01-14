package gov.hhs.onc.dcdt.dns;

public enum DnsServiceType {
    LDAP("_ldap");

    private final String serviceType;

    private DnsServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return this.serviceType;
    }

    public String getServiceType() {
        return this.serviceType;
    }
}
