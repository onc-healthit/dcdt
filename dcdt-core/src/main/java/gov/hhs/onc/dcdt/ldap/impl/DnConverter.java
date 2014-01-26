package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("dnConv")
@ConvertsJson(deserialize = @Converts(from = String.class, to = Dn.class), serialize = @Converts(from = Dn.class, to = String.class))
@List({ @Converts(from = String.class, to = Dn.class), @Converts(from = String[].class, to = Dn.class), @Converts(from = Rdn[].class, to = Dn.class),
    @Converts(from = Dn.class, to = Rdn[].class), @Converts(from = Dn.class, to = String[].class), @Converts(from = Dn.class, to = String.class) })
@Scope("singleton")
public class DnConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_DN = TypeDescriptor.valueOf(Dn.class);
    private final static TypeDescriptor TYPE_DESC_RDN_ARR = TypeDescriptor.array(TypeDescriptor.valueOf(Rdn.class));

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        Rdn[] sourceRdns;

        if (sourceType.isAssignableTo(TYPE_DESC_DN)) {
            Dn sourceDn = (Dn) source;

            sourceRdns = sourceDn.getRdns().toArray(new Rdn[sourceDn.size()]);

            if (targetType.isAssignableTo(TYPE_DESC_RDN_ARR)) {
                return sourceRdns;
            } else {
                String[] targetRdnStrs = new String[sourceRdns.length];

                for (int a = 0; a < targetRdnStrs.length; a++) {
                    targetRdnStrs[a] = sourceRdns[a].getValue().getString();
                }

                return targetRdnStrs;
            }
        } else {
            if (sourceType.isAssignableTo(TYPE_DESC_STR)) {
                return new Dn((String) source);
            } else if (sourceType.isAssignableTo(TYPE_DESC_STR_ARR)) {
                String[] sourceRdnStrs = (String[]) source;

                sourceRdns = new Rdn[sourceRdnStrs.length];

                for (int a = 0; a < sourceRdns.length; a++) {
                    sourceRdns[a] = new Rdn(sourceRdnStrs[a]);
                }
            } else {
                sourceRdns = (Rdn[]) source;
            }

            return new Dn(sourceRdns);
        }
    }
}
