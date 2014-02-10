package gov.hhs.onc.dcdt.testcases.results;

public enum ToolTestcaseResultType {
    CERT_LOOKUP("Certificate lookup"),
    DNS_LOOKUP("DNS lookup"),
    LDAP_CONNECTION("Connection to an LDAP server"),
    LDAP_LOOKUP("LDAP lookup"),
    TCP_CONNECTION("DNS TCP connection request"),
    IPKIX_URL_REDIRECT("IPKIX URL redirect to X.509 certificate data");

    private final String message;

    private ToolTestcaseResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
