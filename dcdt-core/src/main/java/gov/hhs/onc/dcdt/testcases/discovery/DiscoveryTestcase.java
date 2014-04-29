package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
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
    public final static class DiscoveryTestcaseCredentialsExtractor extends AbstractToolTransformer<DiscoveryTestcase, List<DiscoveryTestcaseCredential>> {
        public final static DiscoveryTestcaseCredentialsExtractor INSTANCE = new DiscoveryTestcaseCredentialsExtractor();

        @Nullable
        @Override
        protected List<DiscoveryTestcaseCredential> transformInternal(DiscoveryTestcase discoveryTestcase) throws Exception {
            return discoveryTestcase.getCredentials();
        }
    }

    public final static class DiscoveryTestcaseMailAddressExtractor extends AbstractToolTransformer<DiscoveryTestcase, MailAddress> {
        public final static DiscoveryTestcaseMailAddressExtractor INSTANCE = new DiscoveryTestcaseMailAddressExtractor();

        @Nullable
        @Override
        protected MailAddress transformInternal(DiscoveryTestcase discoveryTestcase) throws Exception {
            return discoveryTestcase.getMailAddress();
        }
    }

    public final static class DiscoveryTestcaseMailAddressPredicate extends AbstractToolPredicate<DiscoveryTestcase> {
        private Set<MailAddress> mailAddrs;

        public DiscoveryTestcaseMailAddressPredicate(MailAddress ... mailAddrs) {
            this(ToolArrayUtils.asSet(mailAddrs));
        }

        public DiscoveryTestcaseMailAddressPredicate(Set<MailAddress> mailAddrs) {
            this.mailAddrs = mailAddrs;
        }

        @Override
        protected boolean evaluateInternal(DiscoveryTestcase discoveryTestcase) throws Exception {
            return this.mailAddrs.contains(discoveryTestcase.getMailAddress());
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
