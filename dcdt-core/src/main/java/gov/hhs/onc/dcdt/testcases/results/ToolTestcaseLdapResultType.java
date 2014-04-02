package gov.hhs.onc.dcdt.testcases.results;

public enum ToolTestcaseLdapResultType {
    LDAP_CONNECTION_SUCCESS("dcdt.testcase.result.ldap.LdapConnectionSuccess.msg"), LDAP_CONNECTION_FAILURE(
        "dcdt.testcase.result.ldap.LdapConnectionFailure.msg"), NO_BASE_DNS("dcdt.testcase.result.ldap.NoBaseDns.msg");

    private final String message;

    private ToolTestcaseLdapResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
