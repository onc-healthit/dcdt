package gov.hhs.onc.dcdt.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ToolIterableUtils {
    @SuppressWarnings({ "unchecked" })
    public static <T> List<T> asList(Iterable<T> iterable) {
        Class<?> iterableClass = iterable.getClass();

        if (List.class.isAssignableFrom(iterableClass)) {
            return (List<T>) iterable;
        }

        if (Collection.class.isAssignableFrom(iterableClass)) {
            return new ArrayList<>((Collection<T>) iterable);
        } else {
            List<T> list = new ArrayList<>();

            for (T elem : iterable) {
                list.add(elem);
            }

            return list;
        }
    }
}
