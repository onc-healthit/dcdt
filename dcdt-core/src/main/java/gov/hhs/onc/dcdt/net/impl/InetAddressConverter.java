package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import java.net.InetAddress;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("inetAddrConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = InetAddress.class) }, serialize = { @Converts(from = InetAddress.class, to = String.class) })
@ConvertsUserType(InetAddressUserType.class)
@List({ @Converts(from = String[].class, to = InetAddress.class), @Converts(from = String.class, to = InetAddress.class),
    @Converts(from = InetAddress.class, to = String.class) })
@Scope("singleton")
public class InetAddressConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_INET_ADDR = TypeDescriptor.valueOf(InetAddress.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_INET_ADDR)) {
            return ((InetAddress) source).getHostAddress();
        } else {
            String[] sourceStrs;

            return (sourceType.isArray() && (ArrayUtils.getLength(source) == 2)) ? ToolInetAddressUtils.getByAddress((sourceStrs = (String[]) source)[0],
                sourceStrs[1]) : ToolInetAddressUtils.getByAddress(null, (String) source);
        }
    }
}
