package gov.hhs.onc.dcdt.utils;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class ToolListUtils {
    @SafeVarargs
    public static <T, U extends List<T>> U addAll(U list, int index, Collection<? extends T> ... collsAdd) {
        return addAll(list, index, Arrays.asList(collsAdd));
    }

    public static <T, U extends List<T>> U addAll(U list, int index, Collection<? extends Collection<? extends T>> collsAdd) {
        if (!ToolCollectionUtils.isEmpty(list)) {
            for (Collection<? extends T> collAdd : collsAdd) {
                if (!ToolCollectionUtils.isEmpty(collAdd)) {
                    list.addAll(index, collAdd);
                }
            }
        }

        return list;
    }

    public static <T, U extends List<T>> U add(U list, int index, T element) {
        if (!ToolCollectionUtils.isEmpty(list)) {
            list.add(index, element);
        }

        return list;
    }

    public static <T, U extends List<T>> T getFirst(U list) {
        return getFirst(list, null);
    }

    public static <T, U extends List<T>> T getFirst(U list, T defaultIfEmpty) {
        return !ToolCollectionUtils.isEmpty(list) ? list.get(0) : defaultIfEmpty;
    }

    public static <T, U extends List<T>> T getLast(U list) {
        return getLast(list, null);
    }

    public static <T, U extends List<T>> T getLast(U list, T defaultIfEmpty) {
        return !ToolCollectionUtils.isEmpty(list) ? list.get(list.size() - 1) : defaultIfEmpty;
    }
}
