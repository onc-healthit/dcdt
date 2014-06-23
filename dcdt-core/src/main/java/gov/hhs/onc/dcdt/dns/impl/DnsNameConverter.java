package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Name;

@Component("convDnsName")
@Converts({ @Convert(from = String[].class, to = Name.class), @Convert(from = Name[].class, to = Name.class), @Convert(from = Name.class, to = String[].class),
    @Convert(from = Name.class, to = Name[].class) })
public class DnsNameConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_NAME = TypeDescriptor.valueOf(Name.class);

    @Nullable
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        if (srcType.isAssignableTo(TYPE_DESC_NAME)) {
            Name srcName = ((Name) src);

            return (targetType.isAssignableTo(TYPE_DESC_STR_ARR) ? ToolDnsNameUtils.toLabelStrings(srcName) : ToolDnsNameUtils.toLabels(srcName));
        } else if (srcType.isAssignableTo(TYPE_DESC_STR_ARR)) {
            return ToolDnsNameUtils.fromLabelStrings(((String[]) src));
        } else {
            return ToolDnsNameUtils.fromLabels(((Name[]) src));
        }
    }
}
