package gov.hhs.onc.dcdt.utils;


import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;

public abstract class ToolListUtils {
    public final static int INDEX_UNKNOWN = -1;

    public static <T> boolean startsWith(@Nullable List<T> list1, @Nullable List<T> list2) {
        if ((list1 == null) || (list2 == null) || (list2.size() > list1.size())) {
            return false;
        }

        for (int a = 0; a < list2.size(); a++) {
            if (!ObjectUtils.equals(list1.get(a), list2.get(a))) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean endsWith(@Nullable List<T> list1, @Nullable List<T> list2) {
        if ((list1 == null) || (list2 == null) || (list2.size() > list1.size())) {
            return false;
        }

        int listStart = list1.size() - list2.size();

        for (int a = 0; a < list2.size(); a++) {
            if (!ObjectUtils.equals(list1.get(listStart + a), list2.get(a))) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAllFirst(@Nullable List<T> list, @Nullable Collection<T> ... colls) {
        return addAllFirst(list, ToolArrayUtils.asList(colls));
    }

    @Nullable
    public static <T> List<T> addAllFirst(@Nullable List<T> list, @Nullable Iterable<? extends Collection<T>> colls) {
        return addAll(list, ToolNumberUtils.defaultIfNegative(getFirstIndex(list), 0), colls);
    }

    @Nullable
    public static <T> List<T> addFirst(@Nullable List<T> list, @Nullable T elem) {
        return add(list, ToolNumberUtils.defaultIfNegative(getFirstIndex(list), 0), elem);
    }

    @Nullable
    public static <T> T getFirst(@Nullable List<T> list) {
        return getFirst(list, null);
    }

    @Nullable
    public static <T> T getFirst(@Nullable List<T> list, @Nullable T defaultElem) {
        return item(list, getFirstIndex(list), defaultElem);
    }

    @Nullable
    public static <T> List<T> removeFirst(@Nullable List<T> list) {
        return remove(list, getFirstIndex(list));
    }

    public static <T> int getFirstIndex(@Nullable List<T> list) {
        return ToolNumberUtils.defaultIfPositive(ToolCollectionUtils.size(list), 0);
    }

    @Nullable
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAllLast(@Nullable List<T> list, @Nullable Collection<T> ... colls) {
        return addAllLast(list, ToolArrayUtils.asList(colls));
    }

    @Nullable
    public static <T> List<T> addAllLast(@Nullable List<T> list, @Nullable Iterable<? extends Collection<T>> colls) {
        return addAll(list, ToolNumberUtils.defaultIfNegative(getLastIndex(list), 0), colls);
    }

    @Nullable
    public static <T> List<T> addLast(@Nullable List<T> list, @Nullable T elem) {
        return add(list, ToolNumberUtils.defaultIfNegative(getLastIndex(list), 0), elem);
    }

    @Nullable
    public static <T> T getLast(@Nullable List<T> list) {
        return getLast(list, null);
    }

    @Nullable
    public static <T> T getLast(@Nullable List<T> list, @Nullable T defaultElem) {
        return item(list, getLastIndex(list), defaultElem);
    }

    @Nullable
    public static <T> List<T> removeLast(@Nullable List<T> list) {
        return remove(list, getLastIndex(list));
    }

    public static <T> int getLastIndex(@Nullable List<T> list) {
        return ToolNumberUtils.defaultIfNegative(ToolCollectionUtils.size(list), INDEX_UNKNOWN + 1) - 1;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAll(@Nullable List<T> list, int index, @Nullable Collection<T> ... collsAdd) {
        return addAll(list, index, ToolArrayUtils.asList(collsAdd));
    }

    @Nullable
    public static <T> List<T> addAll(@Nullable List<T> list, int index, @Nullable Iterable<? extends Collection<T>> collsAdd) {
        if ((list != null) && (collsAdd != null) && !ToolNumberUtils.isNegative(index)) {
            for (Collection<T> collAdd : collsAdd) {
                if (collAdd != null) {
                    list.addAll(index, collAdd);
                }
            }
        }

        return list;
    }

    @Nullable
    public static <T> List<T> add(@Nullable List<T> list, int index, @Nullable T elem) {
        if ((list != null) && !ToolNumberUtils.isNegative(index)) {
            list.add(index, elem);
        }

        return list;
    }

    @Nullable
    public static <T> T item(@Nullable List<T> list, int index, @Nullable T defaultElem) {
        return (!ToolNumberUtils.isNegative(index) && (ToolCollectionUtils.size(list) > index)) ? list.get(index) : defaultElem;
    }

    @Nullable
    public static <T> List<T> remove(@Nullable List<T> list, int index) {
        if (!ToolNumberUtils.isNegative(index) && (ToolCollectionUtils.size(list) > index)) {
            list.remove(index);
        }

        return list;
    }
}
