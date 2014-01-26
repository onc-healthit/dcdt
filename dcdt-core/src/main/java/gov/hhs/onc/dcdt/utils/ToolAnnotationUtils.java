package gov.hhs.onc.dcdt.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class ToolAnnotationUtils {
    public final static String ANNO_ATTR_NAME_DEFAULT = "value";

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getCallsAttributeValue(Class<T> annoClass, Class<U> annoValueClass, Pair<Class<?>, Method> ... calls) {
        return getCallsAttributeValue(annoClass, annoValueClass, null, calls);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getCallsAttributeValue(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Pair<Class<?>, Method> ... calls) {
        return getCallsValue(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation, U> U getCallsValue(Class<T> annoClass, Class<U> annoValueClass, Iterable<Pair<Class<?>, Method>> calls) {
        return getCallsValue(annoClass, annoValueClass, ANNO_ATTR_NAME_DEFAULT, calls);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> U getCallsValue(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Iterable<Pair<Class<?>, Method>> calls) {
        T anno = findCallsAnnotation(annoClass, calls);

        return (anno != null) ? (U) AnnotationUtils.getValue(anno, ObjectUtils.defaultIfNull(annoAttrName, ANNO_ATTR_NAME_DEFAULT)) : null;
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, AnnotatedElement ... annoElems) {
        return getValues(annoClass, annoValueClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        AnnotatedElement ... annoElems) {
        return getValues(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(annoElems));
    }

    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, Iterable<? extends AnnotatedElement> annoElems) {
        return getValues(annoClass, annoValueClass, null, annoElems);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Iterable<? extends AnnotatedElement> annoElems) {
        annoAttrName = (annoAttrName != null) ? annoAttrName : ANNO_ATTR_NAME_DEFAULT;

        List<T> annos = findAnnotations(annoClass, annoElems);
        List<U> annoValues = new ArrayList<>(annos.size());

        for (T anno : annos) {
            annoValues.add(annoValueClass.cast(AnnotationUtils.getValue(anno, annoAttrName)));
        }

        return annoValues;
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, AnnotatedElement ... annoElems) {
        return getValue(annoClass, annoValueClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        AnnotatedElement ... annoElems) {
        return getValue(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(annoElems));
    }

    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, Iterable<? extends AnnotatedElement> annoElems) {
        return getValue(annoClass, annoValueClass, null, annoElems);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Iterable<? extends AnnotatedElement> annoElems) {
        T anno = findAnnotation(annoClass, annoElems);

        return (anno != null) ? annoValueClass.cast(AnnotationUtils.getValue(anno, ObjectUtils.defaultIfNull(annoAttrName, ANNO_ATTR_NAME_DEFAULT))) : null;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getCallsValues(Class<T> annoClass, Class<U> annoValueClass, Pair<Class<?>, Method> ... calls) {
        return getCallsAttributeValues(annoClass, annoValueClass, ANNO_ATTR_NAME_DEFAULT, calls);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getCallsAttributeValues(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Pair<Class<?>, Method> ... calls) {
        return getCallsAttributeValues(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation, U> List<U> getCallsValues(Class<T> annoClass, Class<U> annoValueClass, Iterable<Pair<Class<?>, Method>> calls) {
        return getCallsAttributeValues(annoClass, annoValueClass, ANNO_ATTR_NAME_DEFAULT, calls);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> List<U> getCallsAttributeValues(Class<T> annoClass, Class<U> annoValueClass, @Nullable String annoAttrName,
        Iterable<Pair<Class<?>, Method>> calls) {
        annoAttrName = (annoAttrName != null) ? annoAttrName : ANNO_ATTR_NAME_DEFAULT;

        List<T> annos = findCallsAnnotations(annoClass, calls);
        List<U> annoValues = new ArrayList<>(annos.size());

        for (T anno : annos) {
            annoValues.add(annoValueClass.cast(AnnotationUtils.getValue(anno, annoAttrName)));
        }

        return annoValues;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> List<T> findCallsAnnotations(Class<T> annoClass, Pair<Class<?>, Method> ... calls) {
        return findCallsAnnotations(annoClass, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation> List<T> findCallsAnnotations(Class<T> annoClass, Iterable<Pair<Class<?>, Method>> calls) {
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
    public static <T extends Annotation> T findCallsAnnotation(Class<T> annoClass, Pair<Class<?>, Method> ... calls) {
        return findCallsAnnotation(annoClass, ToolArrayUtils.asList(calls));
    }

    public static <T extends Annotation> T findCallsAnnotation(Class<T> annoClass, Iterable<Pair<Class<?>, Method>> calls) {
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

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> List<T> findAnnotations(Class<T> annoClass, AnnotatedElement ... annoElems) {
        return findAnnotations(annoClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "ConstantConditions" })
    public static <T extends Annotation> List<T> findAnnotations(Class<T> annoClass, Iterable<? extends AnnotatedElement> annoElems) {
        List<T> annos = new ArrayList<>();
        T anno;
        Method annoElemMethod;
        Class<?> annoElemClass, annoElemClassObj;

        for (AnnotatedElement annoElem : annoElems) {
            annoElemClass = annoElem.getClass();

            if ((ToolClassUtils.isAssignable(Method.class, annoElemClass) && ((annoElemMethod = (Method) annoElem) != null) && ((anno =
                findCallsAnnotation(annoClass, new ImmutablePair<Class<?>, Method>(annoElemMethod.getDeclaringClass(), annoElemMethod))) != null))
                || (ToolClassUtils.isAssignable(Class.class, annoElemClass) && ((annoElemClassObj = (Class<?>) annoElem) != null) && ((anno =
                    AnnotationUtils.findAnnotation(annoElemClassObj, annoClass)) != null))
                || ((anno = AnnotationUtils.getAnnotation(annoElem, annoClass)) != null)) {
                annos.add(anno);
            }
        }

        return annos;
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> T findAnnotation(Class<T> annoClass, AnnotatedElement ... annoElems) {
        return findAnnotation(annoClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "ConstantConditions" })
    public static <T extends Annotation> T findAnnotation(Class<T> annoClass, Iterable<? extends AnnotatedElement> annoElems) {
        T anno;
        Method annoElemMethod;
        Class<?> annoElemClass, annoElemClassObj;

        for (AnnotatedElement annoElem : annoElems) {
            annoElemClass = annoElem.getClass();

            if ((ToolClassUtils.isAssignable(Method.class, annoElemClass) && ((annoElemMethod = (Method) annoElem) != null) && ((anno =
                findCallsAnnotation(annoClass, new ImmutablePair<Class<?>, Method>(annoElemMethod.getDeclaringClass(), annoElemMethod))) != null))
                || (ToolClassUtils.isAssignable(Class.class, annoElemClass) && ((annoElemClassObj = (Class<?>) annoElem) != null) && ((anno =
                    AnnotationUtils.findAnnotation(annoElemClassObj, annoClass)) != null))
                || ((anno = AnnotationUtils.getAnnotation(annoElem, annoClass)) != null)) {
                return anno;
            }
        }

        return null;
    }
}
