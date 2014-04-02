package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolDirectAddressBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolDirectAddressBean extends AbstractToolBean implements ToolDirectAddressBean {
    protected MailAddress directAddr;

    @Override
    public boolean hasDirectAddress() {
        return this.directAddr != null;
    }

    @Nullable
    @Override
    @Transient
    public MailAddress getDirectAddress() {
        return this.directAddr;
    }

    @Override
    public void setDirectAddress(@Nullable MailAddress directAddr) {
        this.directAddr = directAddr;
    }
}
