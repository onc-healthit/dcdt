package gov.hhs.onc.dcdt.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public abstract class ToolMethodUtils {
    private final static String CLASS_NAME = ToolClassUtils.getName(ToolMethodUtils.class);

    public static Pair<Class<?>, Method> getCallee() {
        return getCallee(Thread.currentThread());
    }

    public static Pair<Class<?>, Method> getCallee(Thread thread) {
        return getCallee(thread.getStackTrace());
    }

    @SuppressWarnings({ "varargs" })
    public static Pair<Class<?>, Method> getCallee(StackTraceElement ... stackTraceElems) {
        return getCallee(ToolArrayUtils.asList(stackTraceElems));
    }

    public static Pair<Class<?>, Method> getCallee(Iterable<StackTraceElement> stackTraceElems) {
        return ToolListUtils.getFirst(getCalls(stackTraceElems));
    }

    public static Pair<Class<?>, Method> getCaller() {
        return getCaller(Thread.currentThread());
    }

    public static Pair<Class<?>, Method> getCaller(Thread thread) {
        return getCaller(thread.getStackTrace());
    }

    @SuppressWarnings({ "varargs" })
    public static Pair<Class<?>, Method> getCaller(StackTraceElement ... stackTraceElems) {
        return getCaller(ToolArrayUtils.asList(stackTraceElems));
    }

    public static Pair<Class<?>, Method> getCaller(Iterable<StackTraceElement> stackTraceElems) {
        List<Pair<Class<?>, Method>> calls = getCalls(stackTraceElems);

        return (calls.size() >= 2) ? calls.get(1) : null;
    }

    public static List<Pair<Class<?>, Method>> getCalls() {
        return getCalls(Thread.currentThread());
    }

    public static List<Pair<Class<?>, Method>> getCalls(Thread thread) {
        return getCalls(thread.getStackTrace());
    }

    @SuppressWarnings({ "varargs" })
    public static List<Pair<Class<?>, Method>> getCalls(StackTraceElement ... stackTraceElems) {
        return getCalls(ToolArrayUtils.asList(stackTraceElems));
    }

    public static List<Pair<Class<?>, Method>> getCalls(Iterable<StackTraceElement> stackTraceElems) {
        Iterator<StackTraceElement> stackTraceElemsIter = stackTraceElems.iterator();
        StackTraceElement stackTraceElem;
        String stackTraceElemClassName;
        boolean pastUtilsCalls = false;
        List<Pair<Class<?>, Method>> calls = new ArrayList<>();

        while (stackTraceElemsIter.hasNext() && ((stackTraceElem = stackTraceElemsIter.next()) != null)
            && ((stackTraceElemClassName = stackTraceElem.getClassName()) != null)) {
            if (stackTraceElemClassName.equals(CLASS_NAME)) {
                pastUtilsCalls = true;
            } else if (pastUtilsCalls) {
                calls.add(getCall(stackTraceElem));
            }
        }

        return calls;
    }

    public static Pair<Class<?>, Method> getCall(StackTraceElement stackTraceElem) {
        Class<?> callClass = ToolClassUtils.forName(stackTraceElem.getClassName(), false);

        return (callClass != null) ? new MutablePair<Class<?>, Method>(callClass, getMethod(callClass, stackTraceElem.getMethodName(),
            (Iterable<Class<?>>) null)) : null;
    }

    @SuppressWarnings({ "varargs" })
    public static Method getMethod(Class<?> clazz, String methodName, @Nullable Class<?> ... methodParamClasses) {
        return getMethod(clazz, true, methodName, methodParamClasses);
    }

    @SuppressWarnings({ "varargs" })
    public static Method getMethod(Class<?> clazz, boolean hierarchy, String methodName, @Nullable Class<?> ... methodParamClasses) {
        return getMethod(clazz, hierarchy, methodName, ToolArrayUtils.asList(methodParamClasses));
    }

    public static Method getMethod(Class<?> clazz, String methodName, @Nullable Iterable<Class<?>> methodParamClasses) {
        return getMethod(clazz, true, methodName, methodParamClasses);
    }

    public static Method getMethod(Class<?> clazz, boolean hierarchy, String methodName, @Nullable Iterable<Class<?>> methodParamClasses) {
        boolean ignoreMethodParams = (methodParamClasses == null);

        for (Method methodDeclared : getDeclaredMethods(clazz)) {
            if (methodDeclared.getName().equals(methodName)
                && (ignoreMethodParams || ToolClassUtils.isAssignable(ToolArrayUtils.asList(methodDeclared.getParameterTypes()), methodParamClasses))) {
                return methodDeclared;
            }
        }

        if (hierarchy) {
            Set<Class<?>> classHierarchy = ToolClassUtils.getHierarchy(clazz, false);
            Method methodHierarchical;

            if (classHierarchy != null) {
                for (Class<?> classHierarchical : classHierarchy) {
                    if ((methodHierarchical = getMethod(classHierarchical, true, methodName, methodParamClasses)) != null) {
                        return methodHierarchical;
                    }
                }
            }
        }

        return null;
    }

    public static Set<Method> getMethods(Class<?> clazz) {
        return getMethods(clazz, true, null);
    }

    public static Set<Method> getMethods(Class<?> clazz, boolean hierarchy, @Nullable String methodName, @Nullable Class<?> ... methodParamClasses) {
        return getMethods(clazz, hierarchy, methodName, ToolArrayUtils.asList(methodParamClasses));
    }

    public static Set<Method> getMethods(Class<?> clazz, boolean hierarchy, @Nullable String methodName, @Nullable Iterable<Class<?>> methodParamClasses) {
        boolean ignoreMethodName = (methodName == null), ignoreMethodParams = (methodParamClasses == null);
        Set<Method> methods = new LinkedHashSet<>();

        for (Method methodDeclared : getDeclaredMethods(clazz)) {
            if ((ignoreMethodName || methodDeclared.getName().equals(methodName))
                && (ignoreMethodParams || ToolClassUtils.isAssignable(ToolArrayUtils.asList(methodDeclared.getParameterTypes()), methodParamClasses))) {
                methods.add(methodDeclared);
            }
        }

        if (hierarchy) {
            Set<Class<?>> classHierarchy = ToolClassUtils.getHierarchy(clazz, false);

            if (classHierarchy != null) {
                for (Class<?> classHierarchical : classHierarchy) {
                    methods.addAll(getMethods(classHierarchical, true, methodName, methodParamClasses));
                }
            }
        }

        return methods;
    }

    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        try {
            return ToolArrayUtils.asList(clazz.getDeclaredMethods());
        } catch (NoClassDefFoundError ignored) {
            return new ArrayList<>();
        }
    }

    public static boolean isAssignable(@Nullable Method method1, @Nullable Method method2) {
        return isAssignable(method1, method2, false);
    }

    public static boolean isAssignable(@Nullable Method method1, @Nullable Method method2, boolean ignoreMethodParams) {
        return isAssignable(method1, method2, ignoreMethodParams, false);
    }

    public static boolean isAssignable(@Nullable Method method1, @Nullable Method method2, boolean ignoreMethodParams, boolean defaultIfNull) {
        return ((method1 != null) && (method2 != null))
            ? (method1.equals(method2) || (method1.getName().equals(method2.getName()) && (ignoreMethodParams || ToolClassUtils.isAssignable(
                method1.getParameterTypes(), method2.getParameterTypes())))) : defaultIfNull;
    }

    public static String getName(@Nullable Method method) {
        return (method != null) ? method.getName() : null;
    }
}
