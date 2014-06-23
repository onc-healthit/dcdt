package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsCodeIdentifier;
import gov.hhs.onc.dcdt.dns.DnsIdentifier;
import gov.hhs.onc.dcdt.dns.DnsNameLabelIdentifier;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public abstract class ToolDnsUtils {
    @Nullable
    public static <T extends Enum<T> & DnsNameLabelIdentifier> T findByNameLabel(Class<T> enumClass, Name nameLbl) {
        return ToolEnumUtils.findByPropertyValue(enumClass, DnsNameLabelIdentifier.PROP_NAME_NAME_LBL, nameLbl);
    }

    @Nullable
    public static <T extends Enum<T> & DnsCodeIdentifier> T findByCode(Class<T> enumClass, @Nonnegative int code) {
        return ToolEnumUtils.findByPropertyValue(enumClass, DnsCodeIdentifier.PROP_NAME_CODE, code);
    }

    @Nullable
    public static <T extends Enum<T> & DnsIdentifier> T findById(Class<T> enumClass, String id) {
        return ToolEnumUtils.findByPropertyValue(enumClass, DnsIdentifier.PROP_NAME_ID, id);
    }
}
