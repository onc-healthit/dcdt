package gov.hhs.onc.dcdt.testcases.discovery.mail;

import gov.hhs.onc.dcdt.beans.ToolDirectAddressBean;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.Objects;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseMailMapping extends ToolDirectAddressBean {
    public final static class DiscoveryTestcaseMailMappingPredicate extends AbstractToolPredicate<DiscoveryTestcaseMailMapping> {
        private MailAddress mailAddr;

        public DiscoveryTestcaseMailMappingPredicate(MailAddress mailAddr) {
            this.mailAddr = mailAddr;
        }

        @Override
        protected boolean evaluateInternal(DiscoveryTestcaseMailMapping mailMapping) throws Exception {
            return Objects.equals(mailMapping.getDirectAddress(), this.mailAddr);
        }
    }

    public boolean hasResultsAddress();

    @Nullable
    public MailAddress getResultsAddress();

    public void setResultsAddress(@Nullable MailAddress resultsAddr);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String msg);
}
