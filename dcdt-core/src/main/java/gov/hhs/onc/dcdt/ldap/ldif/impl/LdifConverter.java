package gov.hhs.onc.dcdt.ldap.ldif.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdifUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("ldifConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = LdifEntry.class) }, serialize = { @Converts(from = LdifEntry.class, to = String.class) })
@List({ @Converts(from = String.class, to = LdifEntry[].class), @Converts(from = String.class, to = LdifEntry.class),
    @Converts(from = LdifEntry[].class, to = String.class), @Converts(from = LdifEntry.class, to = String.class) })
public class LdifConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_LDIF_ENTRY = TypeDescriptor.valueOf(LdifEntry.class);
    private final static TypeDescriptor TYPE_DESC_LDIF_ENTRY_ARR = TypeDescriptor.array(TYPE_DESC_LDIF_ENTRY);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_LDIF_ENTRY_ARR)) {
            return ToolLdifUtils.writeEntries(((LdifEntry[]) source));
        } else if (sourceType.isAssignableTo(TYPE_DESC_LDIF_ENTRY)) {
            return ToolLdifUtils.writeEntries(((LdifEntry) source));
        } else {
            String sourceStr = ((String) source);

            return (targetType.isAssignableTo(TYPE_DESC_LDIF_ENTRY_ARR)
                ? ToolCollectionUtils.toArray(ToolLdifUtils.readEntries(sourceStr), LdifEntry.class) : ToolLdifUtils.readEntry(sourceStr));
        }
    }
}
