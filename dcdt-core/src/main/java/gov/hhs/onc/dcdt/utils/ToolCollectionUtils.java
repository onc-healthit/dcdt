package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ToolCollectionUtils {
    public static class AssignableTransformer<T> extends AbstractToolTransformer<Object, T> {
        private Class<T> clazz;

        public AssignableTransformer(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Nullable
        @Override
        protected T transformInternal(@Nullable Object obj) throws Exception {
            return (ToolClassUtils.isAssignable(ToolClassUtils.getClass(obj), this.clazz) ? this.clazz.cast(obj) : null);
        }
    }

    public static <T> Collection<T> collectAssignable(Class<T> clazz, @Nullable Iterable<?> ... iterables) {
        return collectAssignable(clazz, ToolArrayUtils.asList(iterables));
    }

    public static <T> Collection<T> collectAssignable(Class<T> clazz, @Nullable Iterable<? extends Iterable<?>> iterables) {
        return collectAssignable(clazz, new ArrayList<T>(), iterables);
    }

    public static <T, U extends Collection<T>> U collectAssignable(Class<T> clazz, U coll, @Nullable Iterable<?> ... iterables) {
        return collectAssignable(clazz, coll, ToolArrayUtils.asList(iterables));
    }

    public static <T, U extends Collection<T>> U collectAssignable(Class<T> clazz, U coll, @Nullable Iterable<? extends Iterable<?>> iterables) {
        return CollectionUtils.collect(ToolIteratorUtils.chainedIterator(iterables), new AssignableTransformer<>(clazz), coll);
    }

    @Nullable
    public static <T, U extends Collection<T>> U nullIfEmpty(@Nullable U coll) {
        return !CollectionUtils.isEmpty(coll) ? coll : null;
    }

    @Nullable
    public static <T> Object[] toArray(@Nullable Collection<? extends T> coll) {
        return (coll != null) ? coll.toArray() : null;
    }

    @Nullable
    public static <T> Object[] toArray(@Nullable Collection<? extends T> coll, @Nullable Object[] defaultIfNull) {
        return (coll != null) ? coll.toArray() : defaultIfNull;
    }

    @Nullable
    public static <T> T[] toArray(@Nullable Collection<? extends T> coll, Class<T> itemClass) {
        return toArray(coll, itemClass, null);
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T> T[] toArray(@Nullable Collection<? extends T> coll, Class<T> itemClass, @Nullable T[] defaultIfNull) {
        return (coll != null) ? coll.toArray((T[]) Array.newInstance(itemClass, coll.size())) : defaultIfNull;
    }

    @Nullable
    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T, U extends Collection<T>> U addAll(@Nullable U coll, Iterable<? extends T> ... iterables) {
        return addAll(coll, ToolArrayUtils.asList(iterables));
    }

    @Nullable
    public static <T, U extends Collection<T>> U addAll(@Nullable U coll, @Nullable Iterable<? extends Iterable<? extends T>> iterables) {
        CollectionUtils.addAll(coll, ToolIteratorUtils.chainedIterator(iterables));

        return coll;
    }

    @Nullable
    public static <T, U extends Collection<T>> U add(@Nullable U coll, @Nullable T element) {
        if (coll != null) {
            coll.add(element);
        }

        return coll;
    }
}
