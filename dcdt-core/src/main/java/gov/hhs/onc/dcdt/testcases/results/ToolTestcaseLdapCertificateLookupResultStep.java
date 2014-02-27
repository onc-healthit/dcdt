package gov.hhs.onc.dcdt.testcases.results;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.name.Dn;
import java.util.List;

public interface ToolTestcaseLdapCertificateLookupResultStep extends ToolTestcaseCertificateResultStep {
    public List<Entry> getSearchResults(List<Dn> baseDns, ExprNode exprNode);
}
