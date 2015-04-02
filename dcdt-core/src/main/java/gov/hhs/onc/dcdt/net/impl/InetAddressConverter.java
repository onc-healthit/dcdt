package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import java.net.InetAddress;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convInetAddr")
@Converts({ @Convert(from = String[].class, to = InetAddress.class) })
public class InetAddressConverter extends AbstractToolConverter {
    @Nullable
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        String[] srcStrs = ((String[]) src);

        return ((srcStrs.length == 2) ? ToolInetAddressUtils.getByAddress(srcStrs[0], srcStrs[1]) : null);
    }
}
