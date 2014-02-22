package gov.hhs.onc.dcdt.ldap;

public enum LdapAttribute {
    MAIL("mail", "0.9.2342.19200300.100.1.3"), USER_CERTIFICATE("userCertificate", "2.5.4.36");

    private final String name;
    private final String oid;

    private LdapAttribute(String name, String oid) {
        this.name = name;
        this.oid = oid;
    }

    public String getName() {
        return this.name;
    }

    public String getOid() {
        return this.oid;
    }
}
