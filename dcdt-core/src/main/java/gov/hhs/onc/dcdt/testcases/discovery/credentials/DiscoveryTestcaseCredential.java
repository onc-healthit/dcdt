package gov.hhs.onc.dcdt.testcases.discovery.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.impl.DiscoveryTestcaseCredentialImpl;
import javax.annotation.Nullable;
import org.springframework.context.MessageSourceAware;

@JsonSubTypes({ @Type(DiscoveryTestcaseCredentialImpl.class) })
public interface DiscoveryTestcaseCredential extends MessageSourceAware, ToolNamedBean {
    public final static class DiscoveryTestcaseCredentialTypePredicate extends AbstractToolPredicate<DiscoveryTestcaseCredential> {
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
        protected boolean evaluateInternal(DiscoveryTestcaseCredential cred) throws Exception {
            // noinspection ConstantConditions
            return cred.hasType() && cred.getType().equals(this.credType);
        }
    }

    public boolean hasBindingType();

    @JsonProperty("bindingType")
    @Nullable
    public BindingType getBindingType();

    public void setBindingType(@Nullable BindingType bindingType);

    public boolean hasCredentialConfig();

    @Nullable
    public CredentialConfig getCredentialConfig();

    public void setCredentialConfig(@Nullable CredentialConfig credConfig);

    public boolean hasCredentialInfo();

    @Nullable
    public CredentialInfo getCredentialInfo();

    public void setCredentialInfo(@Nullable CredentialInfo credInfo);

    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public DiscoveryTestcaseCredentialDescription getDescription();

    public void setDescription(@Nullable DiscoveryTestcaseCredentialDescription desc);

    public boolean hasIssuerCredential();

    @JsonProperty("issuerCred")
    @Nullable
    public DiscoveryTestcaseCredential getIssuerCredential();

    public void setIssuerCredential(@Nullable DiscoveryTestcaseCredential issuerCred);

    public boolean hasLocation();

    @JsonProperty("loc")
    @Nullable
    public DiscoveryTestcaseCredentialLocation getLocation();

    public void setLocation(DiscoveryTestcaseCredentialLocation loc);

    public boolean hasType();

    @JsonProperty("type")
    @Nullable
    public DiscoveryTestcaseCredentialType getType();

    public void setType(@Nullable DiscoveryTestcaseCredentialType type);

    @JsonProperty("valid")
    public boolean isValid();

    public void setValid(boolean valid);
}
