package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convMailAddr")
@Converts({ @Convert(from = String[].class, to = MailAddress.class), @Convert(from = MailAddress.class, to = String[].class) })
public class MailAddressConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_MAIL_ADDR = TypeDescriptor.valueOf(MailAddress.class);

    @Nullable
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        return (srcType.isAssignableTo(TYPE_DESC_MAIL_ADDR) ? ((MailAddress) src).toAddressParts() : new MailAddressImpl(((String[]) src)));
    }
}
