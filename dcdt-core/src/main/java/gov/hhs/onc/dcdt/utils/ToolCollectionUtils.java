package gov.hhs.onc.dcdt.utils;


import java.util.Arrays;
import java.util.Collection;

public abstract class ToolCollectionUtils {
    public final static int SIZE_UNKNOWN = -1;

    @SafeVarargs
    public static <T> Collection<T> addAll(Collection<T> coll, Collection<? extends T> ... collsAdd) {
        return addAll(coll, Arrays.asList(collsAdd));
    }

    public static <T> Collection<T> addAll(Collection<T> coll, Collection<? extends Collection<? extends T>> collsAdd) {
        if (!isEmpty(coll)) {
            for (Collection<? extends T> collAdd : collsAdd) {
                if (!isEmpty(collAdd)) {
                    coll.addAll(collAdd);
                }
            }
        }

        return coll;
    }

    public static <T> Collection<T> add(Collection<T> coll, T element) {
        if (!isEmpty(coll)) {
            coll.add(element);
        }

        return coll;
    }

    public static <T> boolean isEmpty(Collection<T> coll) {
        return (coll == null) || coll.isEmpty();
    }

    public static <T> int size(Collection<T> coll) {
        return (coll != null) ? coll.size() : SIZE_UNKNOWN;
    }
}
