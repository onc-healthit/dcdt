package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.ldap.LdapLookupService;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.name.Dn;
import java.util.List;

public interface ToolTestcaseLdapCertificateLookupResultStep extends ToolTestcaseCertificateResultStep {
    public LdapLookupService getLdapLookupService();

    public void setLdapLookupService(LdapLookupService ldapLookupService);

    public List<Entry> getSearchResults(List<Dn> baseDns, ExprNode exprNode);
}
