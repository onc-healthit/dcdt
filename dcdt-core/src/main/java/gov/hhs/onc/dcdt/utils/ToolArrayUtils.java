package gov.hhs.onc.dcdt.utils;


import org.apache.commons.lang3.ArrayUtils;

public abstract class ToolArrayUtils {
    public static <T> T getFirst(T[] arr) {
        return getFirst(arr, null);
    }

    public static <T> T getFirst(T[] arr, T defaultIfEmpty) {
        return !ArrayUtils.isEmpty(arr) ? arr[0] : defaultIfEmpty;
    }

    public static <T> T getLast(T[] arr) {
        return getLast(arr, null);
    }

    public static <T> T getLast(T[] arr, T defaultIfEmpty) {
        return !ArrayUtils.isEmpty(arr) ? arr[arr.length - 1] : defaultIfEmpty;
    }
}
