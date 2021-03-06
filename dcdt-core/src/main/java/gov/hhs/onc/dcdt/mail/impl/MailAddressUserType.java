package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import org.springframework.stereotype.Component;

@Component("mailAddrUserType")
public class MailAddressUserType extends AbstractStringUserType<MailAddress> {
    public MailAddressUserType() {
        super(MailAddress.class);
    }
}
