package gov.hhs.onc.dcdt.testcases.results;

public enum ToolTestcaseResultType {
    CERT_LOOKUP("Certificate lookup"), DNS_LOOKUP("DNS lookup"), LDAP_CONNECTION("Connection to an LDAP server");

    private final String message;

    private ToolTestcaseResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
