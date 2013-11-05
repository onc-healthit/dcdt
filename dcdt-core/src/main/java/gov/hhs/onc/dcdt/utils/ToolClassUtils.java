package gov.hhs.onc.dcdt.utils;


import org.apache.commons.lang3.ClassUtils;

public abstract class ToolClassUtils {
    public static String getName(Object obj) {
        return getName(obj, null);
    }

    public static String getName(Object obj, String defaultIfNull) {
        return (obj != null) ? getName(obj.getClass(), defaultIfNull) : defaultIfNull;
    }

    public static String getName(Class<?> clazz) {
        return getName(clazz, null);
    }

    public static String getName(Class<?> clazz, String defaultIfNull) {
        return (clazz != null) ? clazz.getName() : defaultIfNull;
    }

    public static String getPackageName(Object obj) {
        return getPackageName(obj, null);
    }

    public static String getPackageName(Object obj, String defaultIfNull) {
        return ClassUtils.getPackageName(obj, defaultIfNull);
    }

    public static String getPackageName(Class<?> clazz) {
        return getPackageName(clazz, null);
    }

    public static String getPackageName(Class<?> clazz, String defaultIfNull) {
        return ClassUtils.getPackageName(clazz, defaultIfNull);
    }

    public static String getPackageName(Package pkg) {
        return getPackageName(pkg, null);
    }

    public static String getPackageName(Package pkg, String defaultIfNull) {
        return (pkg != null) ? pkg.getName() : defaultIfNull;
    }

    public static String getShortName(Object obj) {
        return getShortName(obj, null);
    }

    public static String getShortName(Object obj, String defaultIfNull) {
        return ClassUtils.getShortClassName(obj, defaultIfNull);
    }

    public static String getShortName(Class<?> clazz) {
        return getShortName(clazz, null);
    }

    public static String getShortName(Class<?> clazz, String defaultIfNull) {
        return ClassUtils.getShortClassName(clazz, defaultIfNull);
    }

    public static <T> Class<?> getClass(T obj) {
        return getClass(obj, null);
    }

    public static <T> Class<?> getClass(T obj, Class<?> defaultIfNull) {
        return (obj != null) ? obj.getClass() : defaultIfNull;
    }
}
