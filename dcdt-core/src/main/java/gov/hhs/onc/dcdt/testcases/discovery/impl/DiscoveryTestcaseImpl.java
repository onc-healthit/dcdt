package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialType;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERIA5String;

public class DiscoveryTestcaseImpl extends AbstractToolTestcase<DiscoveryTestcaseDescription> implements DiscoveryTestcase {
    private List<DiscoveryTestcaseCredential> creds;
    private MailAddress mailAddr;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.hasCredentials() && this.hasMailAddress()) {
            BindingType credBindingType;
            GeneralNameType credNameType;
            MailAddress credMailAddrBound;
            CredentialConfig credConfig;
            CertificateConfig credCertConfig;
            CertificateDn credCertConfigSubjDn;
            ToolMultiValueMap<GeneralNameType, ASN1Encodable> credCertConfigSubjAltNames;
            DiscoveryTestcaseCredentialLocation credLoc;

            for (DiscoveryTestcaseCredential cred : this.creds) {
                // noinspection ConstantConditions
                if (cred.hasBindingType() && (credBindingType = cred.getBindingType()).isBound() && ((credNameType = credBindingType.getNameType()) != null)
                    && ((credMailAddrBound = this.mailAddr.forBindingType(credBindingType)) != null)) {
                    // noinspection ConstantConditions
                    if (cred.hasCredentialConfig() && (credConfig = cred.getCredentialConfig()).hasCertificateDescriptor()
                        && (credCertConfig = credConfig.getCertificateDescriptor()).hasSubjectDn()
                        && !(credCertConfigSubjDn = credCertConfig.getSubjectDn()).hasMailAddress()
                        && (!credCertConfig.hasSubjectAltNames() || credCertConfig.getSubjectAltNames().isEmpty(credNameType))) {
                        credCertConfigSubjDn.setMailAddress(credMailAddrBound);

                        if ((credCertConfigSubjAltNames = credCertConfig.getSubjectAltNames()) == null) {
                            credCertConfig.setSubjectAltNames((credCertConfigSubjAltNames = new ToolMultiValueMap<>(new LinkedHashMap<>(1))));
                        }

                        // noinspection ConstantConditions
                        credCertConfigSubjAltNames.put(credNameType, new DERIA5String(credMailAddrBound.toAddress()));
                    }

                    // noinspection ConstantConditions
                    if (cred.hasLocation() && !(credLoc = cred.getLocation()).hasMailAddress()) {
                        credLoc.setMailAddress(credMailAddrBound);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasTargetCredentials() {
        return !this.getTargetCredentials().isEmpty();
    }

    @Override
    public Collection<DiscoveryTestcaseCredential> getTargetCredentials() {
        return (this.hasCredentials() ? this.creds.stream().filter(cred -> (cred.getType() == DiscoveryTestcaseCredentialType.TARGET))
            .collect(Collectors.toList()) : ToolArrayUtils.asList());
    }

    @Override
    public boolean hasBackgroundCredentials() {
        return !this.getBackgroundCredentials().isEmpty();
    }

    @Override
    public Collection<DiscoveryTestcaseCredential> getBackgroundCredentials() {
        return (this.hasCredentials() ? this.creds.stream().filter(cred -> (cred.getType() == DiscoveryTestcaseCredentialType.BACKGROUND))
            .collect(Collectors.toList()) : ToolArrayUtils.asList());
    }

    @Override
    public boolean hasCredentials() {
        return !CollectionUtils.isEmpty(this.creds);
    }

    @Nullable
    @Override
    public List<DiscoveryTestcaseCredential> getCredentials() {
        return this.creds;
    }

    @Override
    public void setCredentials(List<DiscoveryTestcaseCredential> creds) {
        this.creds = creds;
    }

    @Override
    public boolean hasMailAddress() {
        return this.mailAddr != null;
    }

    @Nullable
    @Override
    public MailAddress getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(@Nullable MailAddress mailAddr) {
        this.mailAddr = mailAddr;
    }
}
