package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseImpl;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(DiscoveryTestcaseImpl.class) })
public interface DiscoveryTestcase extends ToolTestcase<DiscoveryTestcaseDescription> {
    public static List<DiscoveryTestcaseCredential> extractCredentials(DiscoveryTestcase discoveryTestcase) {
        return discoveryTestcase.getCredentials();
    }

    public static MailAddress extractMailAddress(DiscoveryTestcase discoveryTestcase) {
        return discoveryTestcase.getMailAddress();
    }

    public static boolean hasMailAddress(DiscoveryTestcase discoveryTestcase, MailAddress ... mailAddrs) {
        return hasMailAddress(discoveryTestcase, ToolArrayUtils.asSet(mailAddrs));
    }

    public static boolean hasMailAddress(DiscoveryTestcase discoveryTestcase, Set<MailAddress> mailAddrs) {
        return mailAddrs.contains(discoveryTestcase.getMailAddress());
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
