package gov.hhs.onc.dcdt.utils;


import java.util.Arrays;
import java.util.Collection;

public abstract class ToolCollectionUtils {
    @SafeVarargs
    public static <T, U extends Collection<T>> U addAll(U coll, Collection<? extends T> ... collsAdd) {
        return addAll(coll, Arrays.asList(collsAdd));
    }

    public static <T, U extends Collection<T>> U addAll(U coll, Collection<? extends Collection<? extends T>> collsAdd) {
        if (!isEmpty(coll)) {
            for (Collection<? extends T> collAdd : collsAdd) {
                if (!isEmpty(collAdd)) {
                    coll.addAll(collAdd);
                }
            }
        }

        return coll;
    }

    public static <T, U extends Collection<T>> U add(U coll, T element) {
        if (!isEmpty(coll)) {
            coll.add(element);
        }

        return coll;
    }

    public static <T, U extends Collection<T>> boolean isEmpty(U coll) {
        return (coll == null) || coll.isEmpty();
    }
}
