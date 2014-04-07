package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.ConvertsUserType;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Name;

@Component("dnsNameConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = Name.class) }, serialize = { @Converts(from = Name.class, to = String.class) })
@ConvertsUserType(DnsNameUserType.class)
@List({ @Converts(from = String[].class, to = Name.class), @Converts(from = String.class, to = Name.class), @Converts(from = Name[].class, to = Name.class),
    @Converts(from = Name.class, to = String[].class), @Converts(from = Name.class, to = String.class), @Converts(from = Name.class, to = Name[].class) })
public class DnsNameConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_NAME = TypeDescriptor.valueOf(Name.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_NAME)) {
            Name sourceName = (Name) source;

            if (targetType.isAssignableTo(TYPE_DESC_STR_ARR)) {
                return ToolDnsNameUtils.toLabelStrings(sourceName);
            } else if (targetType.isAssignableTo(TYPE_DESC_STR)) {
                return Objects.toString(sourceName);
            } else {
                return ToolDnsNameUtils.toLabels(sourceName);
            }
        } else if (sourceType.isAssignableTo(TYPE_DESC_STR_ARR)) {
            return ToolDnsNameUtils.fromLabelStrings((String[]) source);
        } else if (sourceType.isAssignableTo(TYPE_DESC_STR)) {
            return ToolDnsNameUtils.fromString((String) source);
        } else {
            return ToolDnsNameUtils.fromLabels((Name[]) source);
        }
    }
}
