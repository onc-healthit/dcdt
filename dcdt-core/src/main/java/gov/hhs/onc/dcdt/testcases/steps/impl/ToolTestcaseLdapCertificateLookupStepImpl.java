package gov.hhs.onc.dcdt.testcases.steps.impl;

import gov.hhs.onc.dcdt.ldap.ToolCoreSchemaConstants;
import gov.hhs.onc.dcdt.ldap.lookup.LdapEntryLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseLdapCertificateLookupStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseLdapStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
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
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class ToolTestcaseLdapCertificateLookupStepImpl extends AbstractToolTestcaseCertificateStep implements ToolTestcaseLdapCertificateLookupStep,
    ToolTestcaseLdapStep {
    @Autowired
    private LdapLookupService ldapLookupService;

    private List<Dn> baseDns;
    private LdapConnectionConfig ldapConnConfig;

    @Override
    public List<Dn> getBaseDns() {
        return this.baseDns;
    }

    @Override
    public void setBaseDns(List<Dn> baseDns) {
        this.baseDns = baseDns;
    }

    @Override
    public LdapConnectionConfig getLdapConnectionConfig() {
        return this.ldapConnConfig;
    }

    @Override
    public void setLdapConnectionConfig(LdapConnectionConfig ldapConnConfig) {
        this.ldapConnConfig = ldapConnConfig;
    }

    @Override
    public boolean execute(MailAddress directAddr, ToolTestcaseStep prevStep) {
        if (prevStep instanceof ToolTestcaseLdapStep) {
            this.ldapConnConfig = ((ToolTestcaseLdapStep) prevStep).getLdapConnectionConfig();
            this.baseDns = ((ToolTestcaseLdapStep) prevStep).getBaseDns();

            EqualityNode<String> mailAttributeEquality =
                new EqualityNode<>(ToolCoreSchemaConstants.ATTR_TYPE_NAME_MAIL, new StringValue(directAddr.toAddress(this.bindingType)));
            PresenceNode userCertificatePresence = new PresenceNode(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT);
            ExprNode exprNode = new AndNode(mailAttributeEquality, userCertificatePresence);

            List<Entry> entries = this.lookupEntries(exprNode);

            for (Entry entry : entries) {
                Attribute userCertAttr = entry.get(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT);
                if (userCertAttr != null) {
                    try {
                        updateCertificateStatus(userCertAttr.getBytes(), directAddr.forBindingType(this.bindingType));
                    } catch (LdapInvalidAttributeValueException e) {
                        this.setMessage(e.getMessage());
                    }
                }
            }

            if (entries.isEmpty()) {
                this.setCertificateStatus(ToolTestcaseCertificateResultType.NO_CERT);
            }
        }

        return this.getCertificateStatus().equals(ToolTestcaseCertificateResultType.VALID_CERT);
    }

    @Override
    public List<Entry> lookupEntries(ExprNode exprNode) {
        List<Entry> entries = new ArrayList<>();
        LdapEntryLookupResult entryLookupResult;

        for (Dn baseDn : this.baseDns) {
            if (!(entryLookupResult = this.ldapLookupService.lookupEntries(this.ldapConnConfig, baseDn, exprNode)).isSuccess()) {
                this.setMessage(entryLookupResult.getMessage());

                break;
            } else {
                ToolCollectionUtils.addAll(entries, entryLookupResult.getItems());
            }
        }

        return entries;
    }
}
