package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.ldap.ToolCoreSchemaConstants;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapCertificateLookupResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import java.util.ArrayList;
import java.util.List;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.filter.AndNode;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.filter.PresenceNode;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class ToolTestcaseLdapCertificateLookupResultStepImpl extends AbstractToolTestcaseCertificateResultStep implements
    ToolTestcaseLdapCertificateLookupResultStep {
    @Autowired
    private LdapLookupService ldapLookupService;

    private LdapConnectionConfig ldapLookupConnConfig;

    @Override
    public boolean execute(ToolTestcaseResultHolder resultHolder, MailAddress directAddr) {
        this.ldapLookupConnConfig = resultHolder.getLdapConnectionConfig();

        EqualityNode<String> mailAttributeEquality =
            new EqualityNode<>(ToolCoreSchemaConstants.ATTR_TYPE_NAME_MAIL, new StringValue(directAddr.toAddress(this.bindingType)));
        PresenceNode userCertificatePresence = new PresenceNode(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT);
        ExprNode exprNode = new AndNode(mailAttributeEquality, userCertificatePresence);

        List<Entry> searchResults = getSearchResults(resultHolder.getBaseDns(), exprNode);

        for (Entry entry : searchResults) {
            Attribute userCertAttr = entry.get(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT);
            if (userCertAttr != null) {
                try {
                    updateCertificateStatus(userCertAttr.getBytes());
                } catch (LdapInvalidAttributeValueException e) {
                    this.setMessage(e.getMessage());
                }
            }
        }

        if (searchResults.isEmpty()) {
            this.setCertificateStatus(ToolTestcaseCertificateResultType.NO_CERT);
        }
        return this.getCertificateStatus().equals(ToolTestcaseCertificateResultType.VALID_CERT);
    }

    @Override
    public List<Entry> getSearchResults(List<Dn> baseDns, ExprNode exprNode) {
        List<Entry> searchResults = new ArrayList<>();
        for (Dn baseDn : baseDns) {
            try {
                searchResults.addAll(this.ldapLookupService.search(this.ldapLookupConnConfig, baseDn, SearchScope.SUBTREE, exprNode));
            } catch (ToolLdapException e) {
                this.setMessage(e.getMessage());
            }
        }
        return searchResults;
    }
}
