package gov.hhs.onc.dcdt.testcases.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapResultType;

public interface ToolTestcaseLdapConnectionStep extends ToolTestcaseStep {
    @JsonProperty("ldapStatus")
    public ToolTestcaseLdapResultType getLdapStatus();

    public void setLdapStatus(ToolTestcaseLdapResultType ldapStatus);
}
