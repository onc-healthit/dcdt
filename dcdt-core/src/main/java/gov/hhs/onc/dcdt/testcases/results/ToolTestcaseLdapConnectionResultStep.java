package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ToolTestcaseLdapConnectionResultStep extends ToolTestcaseResultStep {
    @JsonProperty("ldapStatus")
    public ToolTestcaseLdapResultType getLdapStatus();

    public void setLdapStatus(ToolTestcaseLdapResultType ldapStatus);
}
