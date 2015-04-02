package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.collections.ToolPredicate;
import gov.hhs.onc.dcdt.dns.DnsCodeIdentifier;
import gov.hhs.onc.dcdt.dns.DnsIdentifier;
import gov.hhs.onc.dcdt.dns.DnsNameLabelIdentifier;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public abstract class ToolDnsUtils {
    public static <T extends Enum<T> & DnsIdentifier> String getId(T enumItem) {
        return getId(enumItem, null);
    }

    public static <T extends Enum<T> & DnsIdentifier> String getId(T enumItem, @Nullable Object defaultId) {
        return ((enumItem != null) ? enumItem.getId() : Objects.toString(defaultId, null));
    }

    @Nullable
    public static <T extends Enum<T> & DnsNameLabelIdentifier> T findByNameLabel(Class<T> enumClass, Name nameLbl) {
        return ToolEnumUtils.findByPredicate(enumClass, ToolPredicate.wrap(nameLblEnum -> Objects.equals(nameLblEnum.getNameLabel(), nameLbl)));
    }

    @Nullable
    public static <T extends Enum<T> & DnsCodeIdentifier> T findByCode(Class<T> enumClass, @Nonnegative int code) {
        return ToolEnumUtils.findByPredicate(enumClass, codeEnum -> Objects.equals(codeEnum.getCode(), code));
    }

    @Nullable
    public static <T extends Enum<T> & DnsIdentifier> T findById(Class<T> enumClass, String id) {
        return ToolEnumUtils.findByPredicate(enumClass, idEnum -> Objects.equals(idEnum.getId(), id));
    }
}
