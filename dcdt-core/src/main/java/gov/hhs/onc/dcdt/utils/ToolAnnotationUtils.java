package gov.hhs.onc.dcdt.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class ToolAnnotationUtils {
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, Pair<Class<?>, Method> ... calls) {
        return getValue(annoClass, annoValueClass, null, calls);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Pair<Class<?>, Method> ... calls) {
        return getValue(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, Iterable<Pair<Class<?>, Method>> calls) {
        return getValue(annoClass, annoValueClass, null, calls);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Iterable<Pair<Class<?>, Method>> calls) {
        T anno = findAnnotation(annoClass, calls);

        return (anno != null) ? (U) AnnotationUtils.getValue(anno, annoAttrName) : null;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, Pair<Class<?>, Method> ... calls) {
        return getValues(annoClass, annoValueClass, null, calls);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Pair<Class<?>, Method> ... calls) {
        return getValues(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, Iterable<Pair<Class<?>, Method>> calls) {
        return getValues(annoClass, annoValueClass, null, calls);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Iterable<Pair<Class<?>, Method>> calls) {
        List<T> annos = findAnnotations(annoClass, calls);
        List<U> annoValues = new ArrayList<>(annos.size());

        for (T anno : annos) {
            annoValues.add((U) AnnotationUtils.getValue(anno, annoAttrName));
        }

        return annoValues;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> List<T> findAnnotations(Class<T> annoClass, Pair<Class<?>, Method> ... calls) {
        return findAnnotations(annoClass, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation> List<T> findAnnotations(Class<T> annoClass, Iterable<Pair<Class<?>, Method>> calls) {
        List<T> annos = new ArrayList<>();
        Method callMethod;
        T anno;

        for (Pair<Class<?>, Method> call : calls) {
            if (((callMethod = call.getRight()) != null) && ((anno = AnnotationUtils.findAnnotation(callMethod, annoClass)) != null)) {
                annos.add(anno);
            }

            if ((anno = AnnotationUtils.findAnnotation(call.getLeft(), annoClass)) != null) {
                annos.add(anno);
            }
        }

        return annos;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> T findAnnotation(Class<T> annoClass, Pair<Class<?>, Method> ... calls) {
        return findAnnotation(annoClass, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation> T findAnnotation(Class<T> annoClass, Iterable<Pair<Class<?>, Method>> calls) {
        Method callMethod;
        T anno;

        for (Pair<Class<?>, Method> call : calls) {
            if ((((callMethod = call.getRight()) != null) && ((anno = AnnotationUtils.findAnnotation(callMethod, annoClass)) != null))
                || ((anno = AnnotationUtils.findAnnotation(call.getLeft(), annoClass)) != null)) {
                return anno;
            }
        }

        return null;
    }
}
