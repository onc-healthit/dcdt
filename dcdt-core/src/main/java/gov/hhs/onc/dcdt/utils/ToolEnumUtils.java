package gov.hhs.onc.dcdt.utils;

import java.util.EnumSet;
import java.util.function.Predicate;

public abstract class ToolEnumUtils {
    public static <T extends Enum<T>> T findByPredicate(Class<T> enumClass, Predicate<T> predicate) {
        return EnumSet.allOf(enumClass).stream().filter(predicate).findFirst().orElse(null);
    }
}
