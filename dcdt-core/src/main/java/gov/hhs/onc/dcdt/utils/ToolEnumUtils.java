package gov.hhs.onc.dcdt.utils;

import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public abstract class ToolEnumUtils {
    @Nullable
    public static <T extends Enum<T>> T findByPredicate(Class<T> enumClass, Predicate<? super T> propPredicate) {
        return Stream.of(enumClass.getEnumConstants()).filter(propPredicate).findFirst().orElse(null);
    }
}
