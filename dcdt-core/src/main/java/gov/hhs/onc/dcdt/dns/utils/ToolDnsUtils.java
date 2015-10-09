package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsCodeIdentifier;
import gov.hhs.onc.dcdt.dns.DnsIdentifier;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class ToolDnsUtils {
    public static <T extends Enum<T> & DnsIdentifier> String getId(T enumItem) {
        return getId(enumItem, null);
    }

    public static <T extends Enum<T> & DnsIdentifier> String getId(T enumItem, @Nullable Object defaultId) {
        return ((enumItem != null) ? enumItem.getId() : Objects.toString(defaultId, null));
    }

    @Nullable
    public static <T extends Enum<T> & DnsCodeIdentifier> T findByCode(Class<T> enumClass, @Nonnegative int code) {
        return Stream.of(enumClass.getEnumConstants()).filter(enumItem -> (enumItem.getCode() == code)).findFirst().orElse(null);
    }
}
