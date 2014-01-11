package gov.hhs.onc.dcdt.utils;

import java.util.Collection;
import javax.annotation.Nullable;

public abstract class ToolCollectionUtils {
    public final static int SIZE_UNKNOWN = -1;

    @Nullable
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T, U extends Collection<T>> U addAll(@Nullable U coll, Collection<? extends T> ... collsAdd) {
        return addAll(coll, ToolArrayUtils.asList(collsAdd));
    }

    @Nullable
    public static <T, U extends Collection<T>> U addAll(@Nullable U coll, @Nullable Iterable<? extends Collection<? extends T>> collsAdd) {
        if ((coll != null) && (collsAdd != null)) {
            for (Collection<? extends T> collAdd : collsAdd) {
                if (!isEmpty(collAdd)) {
                    coll.addAll(collAdd);
                }
            }
        }

        return coll;
    }

    @Nullable
    public static <T, U extends Collection<T>> U add(@Nullable U coll, @Nullable T element) {
        if (coll != null) {
            coll.add(element);
        }

        return coll;
    }

    public static <T> boolean isEmpty(@Nullable Collection<T> coll) {
        return (coll == null) || coll.isEmpty();
    }

    public static <T> int size(@Nullable Collection<T> coll) {
        return (coll != null) ? coll.size() : SIZE_UNKNOWN;
    }
}
