package gov.hhs.onc.dcdt.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ToolCollectionUtils {
    @Nullable
    public static <T> T transformAssignable(@Nullable Object obj, Class<T> clazz) {
        return isAssignable(obj, clazz) ? clazz.cast(obj) : null;
    }

    public static <T> boolean isAssignable(@Nullable Object obj, Class<T> clazz) {
        return ToolClassUtils.isAssignable(ToolClassUtils.getClass(obj), clazz);
    }

    @Nullable
    public static <T> T findAssignable(Class<T> clazz, @Nullable Object ... objs) {
        return findAssignable(clazz, ToolArrayUtils.asList(objs));
    }

    @Nullable
    public static <T> T findAssignable(Class<T> clazz, @Nullable Iterable<?> objs) {
        return clazz.cast(ToolStreamUtils.find(objs, obj -> isAssignable(obj, clazz)));
    }

    public static <T, U extends Collection<T>> U collectAssignable(Class<T> clazz, U coll, Object ... objs) {
        return collectAssignable(clazz, coll, ToolArrayUtils.asList(objs));
    }

    public static <T, U extends Collection<T>> U collectAssignable(Class<T> clazz, U coll, @Nullable Iterable<?> objs) {
        ToolStreamUtils.stream(objs).map(obj -> transformAssignable(obj, clazz)).filter(Objects::nonNull).forEach(coll::add);

        return coll;
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
