package gov.hhs.onc.dcdt.testcases.steps;

import java.util.List;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public interface ToolTestcaseLdapStep extends ToolTestcaseStep {
    public List<Dn> getBaseDns();

    public void setBaseDns(List<Dn> baseDns);

    public LdapConnectionConfig getLdapConnectionConfig();

    public void setLdapConnectionConfig(LdapConnectionConfig ldapConnConfig);
}
