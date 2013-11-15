package gov.hhs.onc.dcdt.utils;


import java.util.Iterator;
import org.apache.commons.lang3.ClassUtils;

public abstract class ToolClassUtils {
    public static boolean isAssignable(Class<?>[] classes1, Class<?>[] classes2) {
        return isAssignable(ToolArrayUtils.asList(classes1), ToolArrayUtils.asList(classes2));
    }
    
    public static boolean isAssignable(Iterable <Class<?>> classes1, Iterable<Class<?>> classes2) {
        Iterator<Class<?>> classesIter1 = classes1.iterator(), classesIter2 = classes2.iterator();
        Class<?> class1, class2;
        
        do {
            class1 = classesIter1.hasNext() ? classesIter1.next() : null;
            class2 = classesIter2.hasNext() ? classesIter2.next() : null;
            
            if ((class1 == null) || (class2 == null)) {
                return (class1 == null) && (class2 == null);
            } else if (!isAssignable(class1, class2)) {
                return false;
            }
        } while (classesIter1.hasNext() && classesIter2.hasNext());
        
        return true;
    }

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
