package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.ToolCodeIdentifier;
import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class ToolEnumUtils {
    public static <T extends Enum<T> & ToolIdentifier> String getId(T enumItem) {
        return getId(enumItem, null);
    }

    public static <T extends Enum<T> & ToolIdentifier> String getId(T enumItem, @Nullable Object defaultId) {
        return ((enumItem != null) ? enumItem.getId() : Objects.toString(defaultId, null));
    }

    @Nullable
    public static <T extends Enum<T> & ToolCodeIdentifier> T findByCode(Class<T> enumClass, @Nonnegative int code) {
        return Stream.of(enumClass.getEnumConstants()).filter(enumItem -> (enumItem.getCode() == code)).findFirst().orElse(null);
    }

    @Nullable
    public static <T extends Enum<T> & ToolIdentifier> T findById(Class<T> enumClass, String id) {
        return findByPredicate(enumClass, enumItem -> enumItem.getId().equals(id));
    }

    @Nullable
    public static <T extends Enum<T>> T findByPredicate(Class<T> enumClass, Predicate<? super T> propPredicate) {
        return Stream.of(enumClass.getEnumConstants()).filter(propPredicate).findFirst().orElse(null);
    }
}
