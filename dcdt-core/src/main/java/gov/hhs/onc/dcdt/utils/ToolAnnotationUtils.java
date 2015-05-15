package gov.hhs.onc.dcdt.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class ToolAnnotationUtils {
    public final static String ANNO_ATTR_NAME_DEFAULT = "value";

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, AnnotatedElement ... annoElems) {
        return getValues(annoClass, annoValueClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, String annoAttrName, AnnotatedElement ... annoElems) {
        return getValues(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(annoElems));
    }

    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, Iterable<? extends AnnotatedElement> annoElems) {
        return getValues(annoClass, annoValueClass, ANNO_ATTR_NAME_DEFAULT, annoElems);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> List<U> getValues(Class<T> annoClass, Class<U> annoValueClass, String annoAttrName,
        Iterable<? extends AnnotatedElement> annoElems) {
        List<T> annos = findAnnotations(annoClass, annoElems);
        List<U> annoValues = new ArrayList<>(annos.size());

        for (T anno : annos) {
            annoValues.add(annoValueClass.cast(AnnotationUtils.getValue(anno, annoAttrName)));
        }

        return annoValues;
    }

    @Nullable
    public static <T extends Annotation, U> U getValue(T anno, Class<U> annoValueClass) {
        return getValue(anno, annoValueClass, ANNO_ATTR_NAME_DEFAULT);
    }

    @Nullable
    public static <T extends Annotation, U> U getValue(T anno, Class<U> annoValueClass, String annoAttrName) {
        Object annoValue = AnnotationUtils.getValue(anno, annoAttrName);

        return (ToolClassUtils.isAssignable(ToolClassUtils.getClass(annoValue), annoValueClass) ? annoValueClass.cast(annoValue) : null);
    }

    @Nullable
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, AnnotatedElement ... annoElems) {
        return getValue(annoClass, annoValueClass, ToolArrayUtils.asList(annoElems));
    }

    @Nullable
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, String annoAttrName, AnnotatedElement ... annoElems) {
        return getValue(annoClass, annoValueClass, annoAttrName, ToolArrayUtils.asList(annoElems));
    }

    @Nullable
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, Iterable<? extends AnnotatedElement> annoElems) {
        return getValue(annoClass, annoValueClass, ANNO_ATTR_NAME_DEFAULT, annoElems);
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T extends Annotation, U> U getValue(Class<T> annoClass, Class<U> annoValueClass, String annoAttrName,
        Iterable<? extends AnnotatedElement> annoElems) {
        T anno = findAnnotation(annoClass, annoElems);

        return (anno != null) ? annoValueClass.cast(AnnotationUtils.getValue(anno, annoAttrName)) : null;
    }

    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> List<T> findAnnotations(Class<T> annoClass, AnnotatedElement ... annoElems) {
        return findAnnotations(annoClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "ConstantConditions" })
    public static <T extends Annotation> List<T> findAnnotations(Class<T> annoClass, Iterable<? extends AnnotatedElement> annoElems) {
        List<T> annos = new ArrayList<>();
        T anno;
        Class<?> annoElemClass;

        for (AnnotatedElement annoElem : annoElems) {
            annoElemClass = annoElem.getClass();

            if ((ToolClassUtils.isAssignable(annoElemClass, Method.class) && ((anno = AnnotationUtils.findAnnotation(((Method) annoElem), annoClass)) != null))
                || (ToolClassUtils.isAssignable(annoElemClass, Class.class) && ((anno = AnnotationUtils.findAnnotation(((Class<?>) annoElem), annoClass)) != null))
                || ((anno = AnnotationUtils.getAnnotation(annoElem, annoClass)) != null)) {
                annos.add(anno);
            }
        }

        return annos;
    }

    @Nullable
    @SuppressWarnings({ "varargs" })
    public static <T extends Annotation> T findAnnotation(Class<T> annoClass, AnnotatedElement ... annoElems) {
        return findAnnotation(annoClass, ToolArrayUtils.asList(annoElems));
    }

    @SuppressWarnings({ "ConstantConditions" })
    public static <T extends Annotation> T findAnnotation(Class<T> annoClass, Iterable<? extends AnnotatedElement> annoElems) {
        T anno;
        Class<?> annoElemClass;

        for (AnnotatedElement annoElem : annoElems) {
            annoElemClass = annoElem.getClass();

            if ((ToolClassUtils.isAssignable(annoElemClass, Method.class) && ((anno = AnnotationUtils.findAnnotation(((Method) annoElem), annoClass)) != null))
                || (ToolClassUtils.isAssignable(annoElemClass, Class.class) && ((anno = AnnotationUtils.findAnnotation(((Class<?>) annoElem), annoClass)) != null))
                || ((anno = AnnotationUtils.getAnnotation(annoElem, annoClass)) != null)) {
                return anno;
            }
        }

        return null;
    }
}
