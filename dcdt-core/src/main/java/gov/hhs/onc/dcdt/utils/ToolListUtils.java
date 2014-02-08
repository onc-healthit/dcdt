package gov.hhs.onc.dcdt.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ToolListUtils {
    @Nullable
    public static <T extends Comparable<T>> List<T> sort(@Nullable List<T> list) {
        if (list == null) {
            return null;
        }

        Collections.sort(list);

        return list;
    }

    @Nullable
    public static <T> List<T> sort(@Nullable List<T> list, Comparator<? super T> comparator) {
        if (list == null) {
            return null;
        }

        Collections.sort(list, comparator);

        return list;
    }

    public static boolean startsWith(@Nullable List<?> list1, @Nullable List<?> list2) {
        if ((list1 == null) || (list2 == null) || (list2.size() > list1.size())) {
            return false;
        }

        for (int a = 0; a < list2.size(); a++) {
            if (!Objects.equals(list1.get(a), list2.get(a))) {
                return false;
            }
        }

        return true;
    }

    public static boolean endsWith(@Nullable List<?> list1, @Nullable List<?> list2) {
        if ((list1 == null) || (list2 == null) || (list2.size() > list1.size())) {
            return false;
        }

        int listStart = list1.size() - list2.size();

        for (int a = 0; a < list2.size(); a++) {
            if (!Objects.equals(list1.get(listStart + a), list2.get(a))) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAllFirst(@Nullable List<T> list, @Nullable Iterable<? extends T> ... iterables) {
        return addAllFirst(list, ToolArrayUtils.asList(iterables));
    }

    @Nullable
    public static <T> List<T> addAllFirst(@Nullable List<T> list, @Nullable Iterable<? extends Iterable<? extends T>> iterables) {
        return addAll(list, 0, iterables);
    }

    @Nullable
    public static <T> List<T> addFirst(@Nullable List<T> list, @Nullable T elem) {
        return add(list, 0, elem);
    }

    @Nullable
    public static <T> T getFirst(@Nullable List<T> list) {
        return getFirst(list, null);
    }

    @Nullable
    public static <T> T getFirst(@Nullable List<T> list, @Nullable T defaultItem) {
        return get(list, 0, defaultItem);
    }

    @Nullable
    public static <T> List<T> removeFirst(@Nullable List<T> list) {
        return remove(list, 0);
    }

    @Nullable
    public static <T> T getLast(@Nullable List<T> list) {
        return getLast(list, null);
    }

    @Nullable
    public static <T> T getLast(@Nullable List<T> list, @Nullable T defaultItem) {
        return get(list, getLastIndex(list), defaultItem);
    }

    @Nullable
    public static <T> List<T> removeLast(@Nullable List<T> list) {
        return remove(list, getLastIndex(list));
    }

    @Nonnegative
    public static <T> int getLastIndex(@Nullable List<T> list) {
        return Math.max((CollectionUtils.size(list) - 1), 0);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAll(@Nullable List<T> list, @Nonnegative int index, @Nullable Iterable<? extends T> ... iterables) {
        return addAll(list, index, ToolArrayUtils.asList(iterables));
    }

    @Nullable
    public static <T> List<T> addAll(@Nullable List<T> list, @Nonnegative int index, @Nullable Iterable<? extends Iterable<? extends T>> iterables) {
        if ((list != null) && (iterables != null)) {
            for (Iterable<? extends T> iterable : iterables) {
                if (iterable != null) {
                    for (T item : iterable) {
                        list.add(index, item);
                    }
                }
            }
        }

        return list;
    }

    @Nullable
    public static <T> List<T> add(@Nullable List<T> list, @Nonnegative int index, @Nullable T elem) {
        if (list != null) {
            list.add(index, elem);
        }

        return list;
    }

    @Nullable
    @SuppressWarnings({ "ConstantConditions" })
    public static <T> T get(@Nullable List<T> list, @Nonnegative int index, @Nullable T defaultElem) {
        return (!CollectionUtils.sizeIsEmpty(list) && (CollectionUtils.size(list) > index)) ? list.get(index) : defaultElem;
    }

    @Nullable
    @SuppressWarnings({ "ConstantConditions" })
    public static <T> List<T> remove(@Nullable List<T> list, @Nonnegative int index) {
        if (!CollectionUtils.sizeIsEmpty(list) && (CollectionUtils.size(list) > index)) {
            list.remove(index);
        }

        return list;
    }
}
