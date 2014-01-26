package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.mail.MailAddress;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("mailAddrConv")
@ConvertsJson(deserialize = @Converts(from = String.class, to = MailAddress.class), serialize = @Converts(from = MailAddress.class, to = String.class))
@ConvertsUserType(MailAddressUserType.class)
@List({ @Converts(from = String[].class, to = MailAddress.class), @Converts(from = String.class, to = MailAddress.class),
    @Converts(from = MailAddress.class, to = String[].class), @Converts(from = MailAddress.class, to = String.class) })
@Scope("singleton")
public class MailAddressConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_MAIL_ADDR = TypeDescriptor.valueOf(MailAddress.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_MAIL_ADDR)) {
            MailAddress sourceMailAddr = (MailAddress) source;

            if (targetType.isAssignableTo(TYPE_DESC_STR_ARR)) {
                return sourceMailAddr.toAddressParts();
            } else {
                return sourceMailAddr.toAddress();
            }
        } else if (sourceType.isAssignableTo(TYPE_DESC_STR_ARR) && (ArrayUtils.getLength(source) == 2)) {
            String[] sourceStrs = (String[]) source;

            return new MailAddressImpl(sourceStrs[0], sourceStrs[1]);
        } else {
            return new MailAddressImpl((String) source);
        }
    }
}
