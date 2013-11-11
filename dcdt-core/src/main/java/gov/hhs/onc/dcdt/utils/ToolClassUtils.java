package gov.hhs.onc.dcdt.utils;


import org.apache.commons.lang3.ClassUtils;

public abstract class ToolClassUtils {
    public static boolean isAssignable(Class<?> class1, Class<?> class2) {
        return (class1 != null) && (class2 != null) && class1.isAssignableFrom(class2);
    }

    public static Class<?> forName(String className) {
        return forName(className, true);
    }

    public static Class<?> forName(String className, boolean initialize) {
        return forName(className, initialize, ToolClassUtils.class.getClassLoader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException ignored) {
        }

        return null;
    }

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
