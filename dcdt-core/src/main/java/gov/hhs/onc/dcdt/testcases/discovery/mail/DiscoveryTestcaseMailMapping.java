package gov.hhs.onc.dcdt.testcases.discovery.mail;

import gov.hhs.onc.dcdt.beans.ToolDirectAddressBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseMailMapping extends ToolDirectAddressBean {
    public boolean hasResultsAddress();

    @Nullable
    public MailAddress getResultsAddress();

    public void setResultsAddress(@Nullable MailAddress resultsAddr);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String msg);
}
