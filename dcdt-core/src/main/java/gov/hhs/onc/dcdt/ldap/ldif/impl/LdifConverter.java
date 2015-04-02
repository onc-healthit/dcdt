package gov.hhs.onc.dcdt.ldap.ldif.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdifUtils;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convLdif")
@Converts({ @Convert(from = LdifEntry[].class, to = String.class) })
public class LdifConverter extends AbstractToolConverter {
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        return ToolLdifUtils.writeEntries(((LdifEntry[]) src));
    }
}
