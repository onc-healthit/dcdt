package gov.hhs.onc.dcdt.dns;

public enum ServiceType {

    LDAP("_ldap");

    private String serviceType;

    private ServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return this.serviceType;
    }

}
