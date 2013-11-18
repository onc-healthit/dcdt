package gov.hhs.onc.dcdt.utils;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    public static Method getMethod(Class<?> clazz, String methodName, @Nullable Class<?> ... argClasses) {
        return getMethod(clazz, methodName, ToolArrayUtils.asList(argClasses));
    }

    public static Method getMethod(Class<?> clazz, String methodName, @Nullable Iterable<Class<?>> argClasses) {
        List<Method> methods = getMethods(clazz);

        if (methods != null) {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    if ((argClasses == null) || ToolClassUtils.isAssignable(ToolArrayUtils.asList(method.getParameterTypes()), argClasses)) {
                        return method;
                    }
                }
            }
        }

        return null;
    }

    public static List<Method> getMethods(Class<?> clazz) {
        try {
            return ToolArrayUtils.asList(clazz.getMethods());
        } catch (NoClassDefFoundError ignored) {
        }

        return null;
    }
}
