package gov.hhs.onc.dcdt.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public abstract class ToolOrderUtils {
    public static class PriorityOrderedQueue<T> extends PriorityQueue<T> {
        /**
         * @see java.util.PriorityQueue.DEFAULT_INITIAL_CAPACITY
         */
        private final static int INITIAL_CAPACITY_DEFAULT = 11;

        private final static long serialVersionUID = 0L;

        public PriorityOrderedQueue() {
            this(INITIAL_CAPACITY_DEFAULT);
        }

        public PriorityOrderedQueue(Collection<? extends T> coll) {
            this(coll.size());

            this.addAll(coll);
        }

        public PriorityOrderedQueue(int initialCapacity) {
            super(initialCapacity, ToolOrderUtils::compare);
        }
    }

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
        Integer orderAnnoValue = ToolAnnotationUtils.getValue(Order.class, Integer.class, clazz);

        return (orderAnnoValue != null) ? orderAnnoValue : defaultIfNone;
    }
}
