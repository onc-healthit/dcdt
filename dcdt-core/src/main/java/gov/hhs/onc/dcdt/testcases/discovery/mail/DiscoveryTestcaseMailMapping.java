package gov.hhs.onc.dcdt.testcases.discovery.mail;

import gov.hhs.onc.dcdt.beans.ToolDirectAddressBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.Objects;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseMailMapping extends ToolDirectAddressBean {
    public static boolean hasMailMapping(DiscoveryTestcaseMailMapping mailMapping, MailAddress mailAddr) {
        return Objects.equals(mailMapping.getDirectAddress(), mailAddr);
    }

    public boolean hasResultsAddress();

    @Nullable
    public MailAddress getResultsAddress();

    public void setResultsAddress(@Nullable MailAddress resultsAddr);
}
