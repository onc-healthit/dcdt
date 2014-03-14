package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("mailAddrConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = MailAddress.class) }, serialize = { @Converts(from = MailAddress.class, to = String.class) })
@ConvertsUserType(MailAddressUserType.class)
@List({ @Converts(from = String[].class, to = MailAddress.class), @Converts(from = String.class, to = MailAddress.class),
    @Converts(from = MailAddress.class, to = String[].class), @Converts(from = MailAddress.class, to = String.class) })
public class MailAddressConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_MAIL_ADDR = TypeDescriptor.valueOf(MailAddress.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_MAIL_ADDR)) {
            MailAddress sourceMailAddr = ((MailAddress) source);

            return (targetType.isAssignableTo(TYPE_DESC_STR_ARR) ? sourceMailAddr.toAddressParts() : sourceMailAddr.toAddress());
        } else {
            String[] sourceStrs = (sourceType.isAssignableTo(TYPE_DESC_STR_ARR) ? ((String[]) source) : ToolMailAddressUtils.splitParts(((String) source)));

            return ((sourceStrs != null) ? new MailAddressImpl(sourceStrs) : null);
        }
    }
}
