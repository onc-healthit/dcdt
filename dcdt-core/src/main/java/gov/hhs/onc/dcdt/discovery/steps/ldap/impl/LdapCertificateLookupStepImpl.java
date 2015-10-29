package gov.hhs.onc.dcdt.discovery.steps.ldap.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.ldap.LdapBaseDnLookupStep;
import gov.hhs.onc.dcdt.discovery.steps.ldap.LdapCertificateLookupStep;
import gov.hhs.onc.dcdt.ldap.ToolCoreSchemaConstants;
import gov.hhs.onc.dcdt.ldap.lookup.LdapBaseDnLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapEntryLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapFilterUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.filter.AndNode;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.filter.OrNode;
import org.apache.directory.api.ldap.model.filter.PresenceNode;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public class LdapCertificateLookupStepImpl extends AbstractLdapLookupStep<Entry, LdapEntryLookupResult> implements LdapCertificateLookupStep {
    private List<CertificateInfo> certInfos;

    public LdapCertificateLookupStepImpl(BindingType bindingType, LdapLookupService lookupService) {
        super(bindingType, lookupService);
    }

    @Nullable
    @Override
    protected LdapEntryLookupResult executeLookup(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        LdapBaseDnLookupStep baseDnLookupStep = ToolCollectionUtils.findAssignable(LdapBaseDnLookupStep.class, prevSteps);
        LdapBaseDnLookupResult baseDnLookupResult;

        // noinspection ConstantConditions
        if (baseDnLookupStep.isSuccess() && (baseDnLookupResult = baseDnLookupStep.getResult()).hasItems()) {
            // noinspection ConstantConditions
            LdapConnectionConfig baseDnConnConfig = baseDnLookupResult.getConnectionConfig();
            AndNode lookupFilter =
                new AndNode(new EqualityNode<>(ToolCoreSchemaConstants.ATTR_TYPE_NAME_MAIL, new StringValue(directAddr.toAddress())), new OrNode(
                    new PresenceNode(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT_BINARY), new PresenceNode(
                        ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT)));
            LdapEntryLookupResult lookupResult = null;
            Attribute attr;
            CertificateInfo certInfo;

            this.certInfos = new ArrayList<>();

            // noinspection ConstantConditions
            for (Dn baseDn : baseDnLookupResult.getItems()) {
                if ((lookupResult = this.lookupService.lookupEntries(baseDnConnConfig, baseDn, lookupFilter)).isSuccess() && lookupResult.hasItems()) {
                    // noinspection ConstantConditions
                    for (Entry entry : lookupResult.getItems()) {
                        for (Value<?> attrValue : (attr =
                            (entry.containsAttribute(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT_BINARY) ? entry
                                .get(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT_BINARY) : entry.get(ToolCoreSchemaConstants.ATTR_TYPE_NAME_USER_CERT)))) {
                            try {
                                this.certInfos.add(certInfo =
                                    new CertificateInfoImpl(CertificateUtils.readCertificate(attrValue.getBytes(), CertificateType.X509, DataEncoding.DER)));
                                this.execMsgs
                                    .add(new ToolMessageImpl(
                                        ToolMessageLevel.INFO,
                                        String
                                            .format(
                                                "LDAP lookup (host=%s, port=%d, filter={%s}) entry (dn={%s}) attribute (id=%s) value certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) processed.",
                                                baseDnConnConfig.getLdapHost(), baseDnConnConfig.getLdapPort(), ToolLdapFilterUtils.writeFilter(lookupFilter),
                                                entry.getDn().getName(), attr.getId(), certInfo.getSubjectDn(), certInfo.getSerialNumber(),
                                                certInfo.getIssuerDn())));
                            } catch (CryptographyException e) {
                                this.execMsgs.add(new ToolMessageImpl(ToolMessageLevel.ERROR, String.format(
                                    "LDAP lookup (host=%s, port=%d, filter={%s}) entry (dn={%s}) attribute (id=%s) value certificate processing failed: %s",
                                    baseDnConnConfig.getLdapHost(), baseDnConnConfig.getLdapPort(), ToolLdapFilterUtils.writeFilter(lookupFilter), entry
                                        .getDn().getName(), attr.getId(), e.getMessage())));
                                this.execSuccess = false;

                                break;
                            }
                        }
                    }
                }
            }

            return lookupResult;
        }

        return null;
    }

    @Override
    public boolean hasCertificateInfos() {
        return !CollectionUtils.isEmpty(this.certInfos);
    }

    @Nullable
    @Override
    public List<CertificateInfo> getCertificateInfos() {
        return this.certInfos;
    }
}
