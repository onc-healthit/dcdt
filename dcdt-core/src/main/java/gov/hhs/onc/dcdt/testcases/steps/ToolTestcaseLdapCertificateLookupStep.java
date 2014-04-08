package gov.hhs.onc.dcdt.testcases.steps;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import java.util.List;

public interface ToolTestcaseLdapCertificateLookupStep extends ToolTestcaseCertificateStep {
    public List<Entry> lookupEntries(ExprNode exprNode);
}
