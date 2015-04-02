package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.ldap.utils.ToolRdnUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convRdn")
@Converts({ @Convert(from = String[].class, to = Rdn.class) })
public class RdnConverter extends AbstractToolConverter {
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        return ToolRdnUtils.fromString(ToolStringUtils.joinDelimit(((String[]) src), ToolRdnUtils.DELIM_RDN));
    }
}
