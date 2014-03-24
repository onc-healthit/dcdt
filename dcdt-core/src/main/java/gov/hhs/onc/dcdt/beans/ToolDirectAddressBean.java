package gov.hhs.onc.dcdt.beans;

import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;

public interface ToolDirectAddressBean extends ToolBean {
    public boolean hasDirectAddress();

    @Nullable
    public MailAddress getDirectAddress();

    public void setDirectAddress(@Nullable MailAddress directAddr);
}
