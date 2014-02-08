package gov.hhs.onc.dcdt.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ToolCollectionUtils {
    @Nullable
    public static <T> Object[] toArray(@Nullable Collection<? extends T> coll) {
        return (coll != null) ? coll.toArray() : null;
    }

    @Nullable
    public static <T> Object[] toArray(@Nullable Collection<? extends T> coll, @Nullable Object[] defaultIfNull) {
        return (coll != null) ? coll.toArray() : defaultIfNull;
    }

    @Nullable
    public static <T> T[] toArray(@Nullable Collection<? extends T> coll, Class<T> itemClass) {
        return toArray(coll, itemClass, null);
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T> T[] toArray(@Nullable Collection<? extends T> coll, Class<T> itemClass, @Nullable T[] defaultIfNull) {
        return (coll != null) ? coll.toArray((T[]) Array.newInstance(itemClass, coll.size())) : defaultIfNull;
    }

    @Nullable
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T, U extends Collection<T>> U addAll(@Nullable U coll, Iterable<? extends T> ... iterables) {
        return addAll(coll, ToolArrayUtils.asList(iterables));
    }

    @Nullable
    public static <T, U extends Collection<T>> U addAll(@Nullable U coll, @Nullable Iterable<? extends Iterable<? extends T>> iterables) {
        CollectionUtils.addAll(coll, ToolIteratorUtils.chainedIterator(iterables));

        return coll;
    }

    @Nullable
    public static <T, U extends Collection<T>> U add(@Nullable U coll, @Nullable T element) {
        if (coll != null) {
            coll.add(element);
        }

        return coll;
    }
}
