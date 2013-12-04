package gov.hhs.onc.dcdt.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public abstract class ToolArrayUtils {
    @SuppressWarnings({ "varargs" })
    public static Object[][] wrapElements(Object... elems) {
        return wrapElements(asList(elems));
    }

    public static Object[][] wrapElements(Iterable<?> elems) {
        List<Object[]> objsArrs = new ArrayList<>();

        for (Object elem : elems) {
            objsArrs.add(ArrayUtils.toArray(elem));
        }

        return objsArrs.toArray(new Object[objsArrs.size()][]);
    }

    public static <T> T[] emptyToNull(T[] arr) {
        return ArrayUtils.isEmpty(arr) ? null : arr;
    }

    public static <T> T getFirst(T[] arr) {
        return getFirst(arr, null);
    }

    public static <T> T getFirst(T[] arr, T defaultIfEmpty) {
        return !ArrayUtils.isEmpty(arr) ? arr[0] : defaultIfEmpty;
    }

    public static <T> T[] removeFirst(T[] arr) {
        return !ArrayUtils.isEmpty(arr) ? ArrayUtils.remove(arr, 0) : arr;
    }

    public static <T> T getLast(T[] arr) {
        return getLast(arr, null);
    }

    public static <T> T getLast(T[] arr, T defaultIfEmpty) {
        return !ArrayUtils.isEmpty(arr) ? arr[arr.length - 1] : defaultIfEmpty;
    }

    public static <T> T[] removeLast(T[] arr) {
        return !ArrayUtils.isEmpty(arr) ? ArrayUtils.remove(arr, arr.length - 1) : arr;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> asList(T ... elems) {
        return (elems != null) ? new ArrayList<>(Arrays.asList(elems)) : new ArrayList<T>();
    }
}
