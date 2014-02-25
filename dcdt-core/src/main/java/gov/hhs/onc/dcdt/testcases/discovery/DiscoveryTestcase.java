package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseImpl;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Transformer;

@JsonSubTypes({ @Type(DiscoveryTestcaseImpl.class) })
public interface DiscoveryTestcase extends
    ToolTestcase<DiscoveryTestcaseResultConfig, DiscoveryTestcaseResultInfo, DiscoveryTestcaseDescription, DiscoveryTestcaseResult> {
    public final static class DiscoveryTestcaseCredentialsExtractor implements Transformer<DiscoveryTestcase, List<DiscoveryTestcaseCredential>> {
        public final static DiscoveryTestcaseCredentialsExtractor INSTANCE = new DiscoveryTestcaseCredentialsExtractor();

        @Nullable
        @Override
        public List<DiscoveryTestcaseCredential> transform(DiscoveryTestcase discoveryTestcase) {
            return discoveryTestcase.getCredentials();
        }
    }

    public boolean hasTargetCredentials();

    @JsonProperty("targetCreds")
    public Collection<DiscoveryTestcaseCredential> getTargetCredentials();

    public boolean hasBackgroundCredentials();

    @JsonProperty("backgroundCreds")
    public Collection<DiscoveryTestcaseCredential> getBackgroundCredentials();

    public boolean hasCredentials();

    @Nullable
    public List<DiscoveryTestcaseCredential> getCredentials();

    public void setCredentials(@Nullable List<DiscoveryTestcaseCredential> creds);

    public boolean hasMailAddress();

    @JsonProperty("mailAddr")
    @Nullable
    public MailAddress getMailAddress();

    public void setMailAddress(@Nullable MailAddress mailAddr);
}
