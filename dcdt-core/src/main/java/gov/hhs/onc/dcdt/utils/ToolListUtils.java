package gov.hhs.onc.dcdt.utils;


import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;

public abstract class ToolListUtils {
    public final static int INDEX_UNKNOWN = -1;

    public static <T> boolean startsWith(List<T> list1, List<T> list2) {
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

    public static <T> boolean endsWith(List<T> list1, List<T> list2) {
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

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAllFirst(List<T> list, Collection<? extends T> ... colls) {
        return addAllFirst(list, ToolArrayUtils.asList(colls));
    }

    public static <T> List<T> addAllFirst(List<T> list, Iterable<? extends Collection<? extends T>> colls) {
        return tryAddAll(list, ToolNumberUtils.defaultIfNegative(getFirstIndex(list), 0), colls);
    }

    public static <T> List<T> addFirst(List<T> list, T elem) {
        return tryAdd(list, ToolNumberUtils.defaultIfNegative(getFirstIndex(list), 0), elem);
    }

    public static <T> T getFirst(List<T> list) {
        return getFirst(list, null);
    }

    public static <T> T getFirst(List<T> list, T defaultElem) {
        return tryGet(list, getFirstIndex(list), defaultElem);
    }

    public static <T> List<T> removeFirst(List<T> list) {
        return tryRemove(list, getFirstIndex(list));
    }

    public static <T> int getFirstIndex(List<T> list) {
        return ToolNumberUtils.defaultIfPositive(ToolCollectionUtils.size(list), 0);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> addAllLast(List<T> list, Collection<? extends T> ... colls) {
        return addAllLast(list, ToolArrayUtils.asList(colls));
    }

    public static <T> List<T> addAllLast(List<T> list, Iterable<? extends Collection<? extends T>> colls) {
        return tryAddAll(list, ToolNumberUtils.defaultIfNegative(getLastIndex(list), 0), colls);
    }

    public static <T> List<T> addLast(List<T> list, T elem) {
        return tryAdd(list, ToolNumberUtils.defaultIfNegative(getLastIndex(list), 0), elem);
    }

    public static <T> T getLast(List<T> list) {
        return getLast(list, null);
    }

    public static <T> T getLast(List<T> list, T defaultElem) {
        return tryGet(list, getLastIndex(list), defaultElem);
    }

    public static <T> List<T> removeLast(List<T> list) {
        return tryRemove(list, getLastIndex(list));
    }

    public static <T> int getLastIndex(List<T> list) {
        return ToolNumberUtils.defaultIfNegative(ToolCollectionUtils.size(list), INDEX_UNKNOWN + 1) - 1;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> tryAddAll(List<T> list, int index, Collection<? extends T> ... colls) {
        return tryAddAll(list, index, ToolArrayUtils.asList(colls));
    }

    public static <T> List<T> tryAddAll(List<T> list, int index, Iterable<? extends Collection<? extends T>> colls) {
        if ((list != null) && !ToolNumberUtils.isNegative(index)) {
            for (Collection<? extends T> coll : colls) {
                if (coll != null) {
                    list.addAll(index, coll);
                }
            }
        }

        return list;
    }

    public static <T> List<T> tryAdd(List<T> list, int index, T elem) {
        if ((list != null) && !ToolNumberUtils.isNegative(index)) {
            list.add(index, elem);
        }

        return list;
    }

    public static <T> T tryGet(List<T> list, int index, T defaultElem) {
        return (!ToolNumberUtils.isNegative(index) && (ToolCollectionUtils.size(list) > index)) ? list.get(index) : defaultElem;
    }

    public static <T> List<T> tryRemove(List<T> list, int index) {
        if (!ToolNumberUtils.isNegative(index) && (ToolCollectionUtils.size(list) > index)) {
            list.remove(index);
        }

        return list;
    }
}
