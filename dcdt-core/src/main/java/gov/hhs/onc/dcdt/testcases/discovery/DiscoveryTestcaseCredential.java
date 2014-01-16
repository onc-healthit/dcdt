package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.testcases.BindingType;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Predicate;

public interface DiscoveryTestcaseCredential extends ToolNamedBean {
    final static class DiscoveryTestcaseCredentialTypePredicate implements Predicate<DiscoveryTestcaseCredential> {
        public final static DiscoveryTestcaseCredentialTypePredicate INSTANCE_CA = new DiscoveryTestcaseCredentialTypePredicate(
            DiscoveryTestcaseCredentialType.CA);
        public final static DiscoveryTestcaseCredentialTypePredicate INSTANCE_BACKGROUND = new DiscoveryTestcaseCredentialTypePredicate(
            DiscoveryTestcaseCredentialType.BACKGROUND);
        public final static DiscoveryTestcaseCredentialTypePredicate INSTANCE_TARGET = new DiscoveryTestcaseCredentialTypePredicate(
            DiscoveryTestcaseCredentialType.TARGET);

        private DiscoveryTestcaseCredentialType credType;

        private DiscoveryTestcaseCredentialTypePredicate(DiscoveryTestcaseCredentialType credType) {
            this.credType = credType;
        }

        @Override
        public boolean evaluate(DiscoveryTestcaseCredential cred) {
            return cred.hasType() && cred.getType().equals(this.credType);
        }
    }

    public boolean hasBindingType();

    @Nullable
    public BindingType getBindingType();

    public void setBindingType(@Nullable BindingType bindingType);

    public boolean hasCertificateData();

    @Nullable
    public byte[] getCertificateData() throws CryptographyException;

    public void setCertificateData(@Nullable byte[] certData) throws CryptographyException;

    public boolean hasCredentialConfig();

    @Nullable
    public CredentialConfig getCredentialConfig();

    public void setCredentialConfig(@Nullable CredentialConfig credConfig);

    public boolean hasCredentialInfo();

    @Nullable
    public CredentialInfo getCredentialInfo();

    public void setCredentialInfo(@Nullable CredentialInfo credInfo);

    public boolean hasDescription();

    @Nullable
    public DiscoveryTestcaseCredentialDescription getDescription();

    public void setDescription(@Nullable DiscoveryTestcaseCredentialDescription desc);

    public boolean hasIssuerCredential();

    @Nullable
    public DiscoveryTestcaseCredential getIssuerCredential();

    public void setIssuerCredential(@Nullable DiscoveryTestcaseCredential issuerCred);

    public boolean hasLocation();

    @Nullable
    public DiscoveryTestcaseCredentialLocation getLocation();

    public void setLocation(DiscoveryTestcaseCredentialLocation loc);

    public boolean hasPrivateKeyData();

    @Nullable
    public byte[] getPrivateKeyData() throws CryptographyException;

    public void setPrivateKeyData(@Nullable byte[] privateKeyData) throws CryptographyException;

    public boolean hasType();

    @Nullable
    public DiscoveryTestcaseCredentialType getType();

    public void setType(@Nullable DiscoveryTestcaseCredentialType type);

    public boolean isValid();

    public void setValid(boolean valid);
}
