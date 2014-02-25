package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.ldap.LdapLookupService;

public interface ToolTestcaseLdapConnectionResultStep extends ToolTestcaseResultStep {
    public LdapLookupService getLdapLookupService();

    public void setLdapLookupService(LdapLookupService ldapLookupService);

    @JsonProperty("ldapStatus")
    public ToolTestcaseLdapResultType getLdapStatus();

    public void setLdapStatus(ToolTestcaseLdapResultType ldapStatus);
}
