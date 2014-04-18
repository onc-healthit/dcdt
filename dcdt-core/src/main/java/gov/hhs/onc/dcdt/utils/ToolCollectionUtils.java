package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import java.lang.reflect.Array;
import java.util.Collection;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;

public abstract class ToolCollectionUtils {
    public static class AssignableTransformer<T> extends AbstractToolTransformer<Object, T> {
        private AssignablePredicate<T> predicate;

        public AssignableTransformer(Class<T> clazz) {
            this.predicate = new AssignablePredicate<>(clazz);
        }

        @Nullable
        @Override
        protected T transformInternal(@Nullable Object obj) throws Exception {
            return (this.predicate.evaluate(obj) ? this.predicate.clazz.cast(obj) : null);
        }
    }

    public static class AssignablePredicate<T> extends AbstractToolPredicate<Object> {
        private Class<T> clazz;

        public AssignablePredicate(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        protected boolean evaluateInternal(@Nullable Object obj) throws Exception {
            return ToolClassUtils.isAssignable(ToolClassUtils.getClass(obj), this.clazz);
        }
    }

    @Nullable
    public static <T> T findAssignable(Class<T> clazz, @Nullable Object ... objs) {
        return findAssignable(clazz, ToolArrayUtils.asList(objs));
    }

    @Nullable
    public static <T> T findAssignable(Class<T> clazz, @Nullable Iterable<?> objs) {
        return clazz.cast(CollectionUtils.find(objs, new AssignablePredicate<>(clazz)));
    }

    public static <T, U extends Collection<T>> U collectAssignable(Class<T> clazz, U coll, Object ... objs) {
        return collectAssignable(clazz, coll, ToolArrayUtils.asList(objs));
    }

    public static <T, U extends Collection<T>> U collectAssignable(Class<T> clazz, U coll, @Nullable Iterable<?> objs) {
        return CollectionUtils.select(CollectionUtils.collect(objs, new AssignableTransformer<>(clazz)), PredicateUtils.notNullPredicate(), coll);
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
