package gov.hhs.onc.dcdt.utils;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;

public abstract class ToolClassUtils {
    public static boolean isAssignable(@Nullable Class<?>[] classes1, @Nullable Class<?>[] classes2) {
        return isAssignable(classes1, classes2, false);
    }

    public static boolean isAssignable(@Nullable Class<?>[] classes1, @Nullable Class<?>[] classes2, boolean defaultIfNull) {
        return isAssignable(ToolArrayUtils.asList(classes1), ToolArrayUtils.asList(classes2), defaultIfNull);
    }

    public static boolean isAssignable(@Nullable Iterable<Class<?>> classes1, @Nullable Iterable<Class<?>> classes2) {
        return isAssignable(classes1, classes2, false);
    }

    public static boolean isAssignable(@Nullable Iterable<Class<?>> classes1, @Nullable Iterable<Class<?>> classes2, boolean defaultIfNull) {
        if ((classes1 == null) || (classes2 == null)) {
            return defaultIfNull;
        }

        Iterator<Class<?>> classesIter1 = classes1.iterator(), classesIter2 = classes2.iterator();

        do {
            if (!isAssignable((classesIter1.hasNext() ? classesIter1.next() : null), (classesIter2.hasNext() ? classesIter2.next() : null))) {
                return false;
            }
        } while (classesIter1.hasNext() && classesIter2.hasNext());

        return true;
    }

    public static boolean isAssignable(@Nullable Class<?> class1, @Nullable Class<?> class2) {
        return isAssignable(class1, class2, false);
    }

    public static boolean isAssignable(@Nullable Class<?> class1, @Nullable Class<?> class2, boolean defaultIfNull) {
        return ((class1 != null) && (class2 != null)) ? class1.isAssignableFrom(class2) : defaultIfNull;
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

    public static List<Class<?>> filter(int mods, Class<?> ... classes) {
        return filter(mods, ToolArrayUtils.asList(classes));
    }

    public static List<Class<?>> filter(int mods, Iterable<Class<?>> classes) {
        List<Class<?>> classesFiltered = new ArrayList<>();

        for (Class<?> clazz : classes) {
            if (hasModifiers(clazz, mods)) {
                classesFiltered.add(clazz);
            }
        }

        return classesFiltered;
    }

    public static boolean hasPrivateAccess(Class<?> clazz) {
        return hasModifiers(clazz, Modifier.PRIVATE);
    }
    
    public static boolean hasProtectedAccess(Class<?> clazz) {
        return hasModifiers(clazz, Modifier.PROTECTED);
    }
    
    public static boolean hasPackageAccess(Class<?> clazz) {
        return hasModifiers(clazz, Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE);
    }
    
    public static boolean hasPublicAccess(Class<?> clazz) {
        return hasModifiers(clazz, Modifier.PUBLIC);
    }
    
    public static boolean hasModifiers(Class<?> clazz, int mods) {
        return ToolReflectionUtils.hasModifiers(mods, clazz.getModifiers());
    }
    
    @Nullable
    public static Set<Class<?>> getHierarchy(@Nullable Class<?> clazz) {
        return getHierarchy(clazz, true);
    }
    
    @Nullable
    public static Set<Class<?>> getHierarchy(@Nullable Class<?> clazz, boolean includeRootClass) {
        if (clazz == null) {
            return null;
        }
        
        Set<Class<?>> classHierarchy = new LinkedHashSet<>();
        
        if (includeRootClass) {
            classHierarchy.add(clazz);
        }
        
        ToolCollectionUtils.addAll(classHierarchy, getHierarchy(clazz.getSuperclass()));
        
        for (Class<?> classInterface : clazz.getInterfaces()) {
            ToolCollectionUtils.addAll(classHierarchy, getHierarchy(classInterface));
        }
        
        return classHierarchy;
    }
}
