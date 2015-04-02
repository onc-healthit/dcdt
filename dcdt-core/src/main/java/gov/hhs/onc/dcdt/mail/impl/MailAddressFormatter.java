package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import java.util.Locale;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component("formatterMailAddr")
public class MailAddressFormatter extends AbstractToolFormatter<MailAddress> {
    public MailAddressFormatter() {
        super(MailAddress.class);
    }

    @Nullable
    @Override
    protected String printInternal(MailAddress obj, Locale locale) throws Exception {
        return obj.toAddress();
    }

    @Nullable
    @Override
    protected MailAddress parseInternal(String str, Locale locale) throws Exception {
        return new MailAddressImpl(ToolMailAddressUtils.splitParts(str));
    }
}
