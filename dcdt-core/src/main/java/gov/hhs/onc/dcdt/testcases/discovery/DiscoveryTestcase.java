package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseImpl;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(DiscoveryTestcaseImpl.class) })
public interface DiscoveryTestcase extends ToolTestcase<DiscoveryTestcaseDescription, DiscoveryTestcaseResult> {
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
    public String getMailAddress();

    public void setMailAddress(@Nullable String mailAddr);
}
