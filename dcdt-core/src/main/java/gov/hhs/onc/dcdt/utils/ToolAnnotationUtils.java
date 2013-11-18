package gov.hhs.onc.dcdt.utils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class ToolAnnotationUtils {
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
