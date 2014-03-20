package gov.hhs.onc.dcdt.utils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.convert.TypeDescriptor;

public abstract class ToolClassUtils {
    public static boolean isAssignable(@Nullable Collection<Class<?>> classes1, @Nullable Collection<Class<?>> classes2) {
        return isAssignable(ToolCollectionUtils.toArray(classes1, Class.class), ToolCollectionUtils.toArray(classes2, Class.class));
    }

    public static boolean isAssignable(@Nullable Class<?>[] classes1, @Nullable Class<?> ... classes2) {
        return (classes1 != null) && ClassUtils.isAssignable(classes1, classes2);
    }

    public static boolean isAssignable(@Nullable Class<?> class1, @Nullable Class<?> class2) {
        return (class1 != null) && ClassUtils.isAssignable(class1, class2);
    }

    @Nullable
    public static Class<?> forName(String className) {
        return forName(className, true);
    }

    @Nullable
    public static Class<?> forName(String className, boolean initialize) {
        return forName(className, initialize, ToolClassUtils.class.getClassLoader());
    }

    @Nullable
    public static Class<?> forName(String className, boolean initialize, ClassLoader classLoader) {
        try {
            return Class.forName(className, initialize, classLoader);
        } catch (ClassNotFoundException ignored) {
        }

        return null;
    }

    @Nullable
    public static String getName(@Nullable Object obj) {
        return getName(obj, null);
    }

    @Nullable
    public static String getName(@Nullable Object obj, @Nullable String defaultIfNull) {
        return (obj != null) ? getName(obj.getClass(), defaultIfNull) : defaultIfNull;
    }

    @Nullable
    public static String getName(@Nullable TypeDescriptor typeDesc) {
        return getName(typeDesc, null);
    }

    @Nullable
    public static String getName(@Nullable TypeDescriptor typeDesc, @Nullable String defaultIfNull) {
        return (typeDesc != null) ? getName(typeDesc.getObjectType(), defaultIfNull) : defaultIfNull;
    }

    @Nullable
    public static String getName(@Nullable Class<?> clazz) {
        return getName(clazz, null);
    }

    @Nullable
    public static String getName(@Nullable Class<?> clazz, @Nullable String defaultIfNull) {
        return (clazz != null) ? clazz.getName() : defaultIfNull;
    }

    @Nullable
    public static String getShortName(@Nullable Object obj) {
        return getShortName(obj, null);
    }

    @Nullable
    public static String getShortName(@Nullable Object obj, @Nullable String defaultIfNull) {
        return ClassUtils.getShortClassName(obj, defaultIfNull);
    }

    @Nullable
    public static String getShortName(@Nullable TypeDescriptor typeDesc) {
        return getShortName(typeDesc, null);
    }

    @Nullable
    public static String getShortName(@Nullable TypeDescriptor typeDesc, @Nullable String defaultIfNull) {
        return (typeDesc != null) ? getShortName(typeDesc.getObjectType(), defaultIfNull) : defaultIfNull;
    }

    @Nullable
    public static String getShortName(@Nullable Class<?> clazz) {
        return getShortName(clazz, null);
    }

    @Nullable
    public static String getShortName(@Nullable Class<?> clazz, @Nullable String defaultIfNull) {
        return ClassUtils.getShortClassName(clazz, defaultIfNull);
    }

    @Nullable
    public static <T> Class<?> getClass(@Nullable T obj) {
        return getClass(obj, null);
    }

    @Nullable
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

    public static boolean isImplementation(Class<?> baseClass, Class<?> clazz) {
        return isImplementation(baseClass, clazz, true);
    }

    public static boolean isImplementation(Class<?> baseClass, Class<?> clazz, boolean onlyConcrete) {
        return isAssignable(clazz, baseClass) && (!onlyConcrete || isConcrete(clazz));
    }

    public static boolean isConcrete(Class<?> clazz) {
        return !clazz.isInterface() && !isAbstract(clazz);
    }

    public static boolean isAbstract(Class<?> clazz) {
        return hasModifiers(clazz, Modifier.ABSTRACT);
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
