package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public abstract class ToolArrayUtils {
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> unwrapElements(T[] ... elemsArrs) {
        return unwrapElements(asList(elemsArrs));
    }
    
    public static <T> List<T> unwrapElements(Iterable<T[]> elemsArrs) {
        List<T> elems = new ArrayList<>();
        
        for (T[] elemsArr : elemsArrs) {
            ToolCollectionUtils.addAll(elems, asList(elemsArr));
        }
        
        return elems;
    }
    
    @SuppressWarnings({ "varargs" })
    public static Object[][] wrapElements(Object ... elems) {
        return wrapElements(asList(elems));
    }

    public static Object[][] wrapElements(Iterable<?> elems) {
        List<Object[]> objsArrs = new ArrayList<>();

        for (Object elem : elems) {
            objsArrs.add(ArrayUtils.toArray(elem));
        }

        return objsArrs.toArray(new Object[objsArrs.size()][]);
    }

    @Nullable
    public static <T> T[] emptyToNull(@Nullable T[] arr) {
        return ArrayUtils.isEmpty(arr) ? null : arr;
    }

    @Nullable
    public static <T> T getFirst(@Nullable T[] arr) {
        return getFirst(arr, null);
    }

    @Nullable
    public static <T> T getFirst(@Nullable T[] arr, @Nullable T defaultIfEmpty) {
        return !ArrayUtils.isEmpty(arr) ? arr[0] : defaultIfEmpty;
    }

    @Nullable
    public static <T> T[] removeFirst(@Nullable T[] arr) {
        return !ArrayUtils.isEmpty(arr) ? ArrayUtils.remove(arr, 0) : arr;
    }

    @Nullable
    public static <T> T getLast(@Nullable T[] arr) {
        return getLast(arr, null);
    }

    @Nullable
    public static <T> T getLast(@Nullable T[] arr, @Nullable T defaultIfEmpty) {
        return !ArrayUtils.isEmpty(arr) ? arr[arr.length - 1] : defaultIfEmpty;
    }

    public static <T> T[] removeLast(@Nullable T[] arr) {
        return !ArrayUtils.isEmpty(arr) ? ArrayUtils.remove(arr, arr.length - 1) : arr;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T> List<T> asList(@Nullable T ... elems) {
        return (elems != null) ? new ArrayList<>(Arrays.asList(elems)) : new ArrayList<T>();
    }
}
