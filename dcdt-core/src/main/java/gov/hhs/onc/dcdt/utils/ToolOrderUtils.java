package gov.hhs.onc.dcdt.utils;

import java.lang.reflect.Method;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public abstract class ToolOrderUtils {
    public static int compare(Object obj1, Object obj2) {
        return compare(ToolClassUtils.getClass(obj1), ToolClassUtils.getClass(obj2));
    }

    public static int compare(Class<?> class1, Class<?> class2) {
        return Integer.compare(getOrder(class1), getOrder(class2));
    }

    public static int getOrder(Object obj) {
        return getOrder(ToolClassUtils.getClass(obj));
    }

    public static int getOrder(Class<?> clazz) {
        return getOrder(clazz, Ordered.LOWEST_PRECEDENCE);
    }

    public static int getOrder(Object obj, int defaultIfNone) {
        return getOrder(ToolClassUtils.getClass(obj), defaultIfNone);
    }

    public static int getOrder(Class<?> clazz, int defaultIfNone) {
        Integer orderAnnoValue = ToolAnnotationUtils.getValue(Order.class, Integer.class, new ImmutablePair<Class<?>, Method>(clazz, null));

        return (orderAnnoValue != null) ? orderAnnoValue : defaultIfNone;
    }
}
