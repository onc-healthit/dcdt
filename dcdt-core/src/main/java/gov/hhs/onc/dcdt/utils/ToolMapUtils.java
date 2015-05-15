package gov.hhs.onc.dcdt.utils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

public abstract class ToolMapUtils {
    public static Map<?, ?> toMap(@Nullable Iterable<?> items) {
        return toMap(IteratorUtils.toArray(IteratorUtils.getIterator(items)));
    }

    public static Map<?, ?> toMap(@Nullable Object ... items) {
        return toMap(Object.class, Object.class, items);
    }

    public static <T, U> Map<T, U> toMap(Class<T> keyClass, Class<U> valueClass, @Nullable Iterable<?> items) {
        return toMap(keyClass, valueClass, IteratorUtils.toArray(IteratorUtils.getIterator(items)));
    }

    public static <T, U> Map<T, U> toMap(Class<T> keyClass, Class<U> valueClass, @Nullable Object ... items) {
        return putAll(new HashMap<T, U>(ArrayUtils.getLength(items)), items);
    }

    @Nullable
    public static <T, U, V extends Map<T, U>> V putAll(@Nullable V map, @Nullable Iterable<?> items) {
        return putAll(map, IteratorUtils.toArray(IteratorUtils.getIterator(items)));
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T, U, V extends Map<T, U>> V putAll(@Nullable V map, @Nullable Object ... items) {
        return (map != null) ? (V) MapUtils.putAll(map, items) : null;
    }
}
