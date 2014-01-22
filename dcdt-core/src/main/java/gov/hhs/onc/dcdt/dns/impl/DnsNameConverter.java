package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Name;

@Component("dnsNameConv")
@List({ @Converts(from = String[].class, to = Name.class), @Converts(from = String.class, to = Name.class), @Converts(from = Name[].class, to = Name.class),
    @Converts(from = Name.class, to = String[].class), @Converts(from = Name.class, to = Name[].class) })
@Scope("singleton")
public class DnsNameConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_NAME = TypeDescriptor.valueOf(Name.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_NAME)) {
            return targetType.isAssignableTo(TYPE_DESC_STR_ARR) ? ToolDnsNameUtils.toLabelStrings((Name) source) : ToolDnsNameUtils.toLabels((Name) source);
        } else if (sourceType.isAssignableTo(TYPE_DESC_STR_ARR)) {
            return ToolDnsNameUtils.fromLabelStrings((String[]) source);
        } else if (sourceType.isAssignableTo(TYPE_DESC_STR)) {
            return ToolDnsNameUtils.fromString((String) source);
        } else {
            return ToolDnsNameUtils.fromLabels((Name[]) source);
        }
    }
}
