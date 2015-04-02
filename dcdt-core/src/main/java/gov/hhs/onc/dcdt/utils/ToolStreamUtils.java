package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

public abstract class ToolStreamUtils {
    public static <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
        return stream(iterable, false);
    }

    public static <T> Stream<T> stream(@Nullable Iterable<T> iterable, boolean parallel) {
        return iterable != null ? StreamSupport.stream(iterable.spliterator(), parallel) : Stream.empty();
    }

    public static <T, U> Collection<U> transform(@Nullable Iterable<T> iterable, @Nullable Function<? super T, ? extends U> mapper) {
        return transform(iterable, mapper, ArrayList::new);
    }

    public static <T, U, V extends Collection<U>> Collection<U> transform(@Nullable Iterable<T> iterable, @Nullable Function<? super T, ? extends U> mapper,
        Supplier<V> collFactory) {
        return transform(iterable, mapper, collFactory, new ArrayList<>());
    }

    public static <T, U, V extends Collection<U>> Collection<U> transform(@Nullable Iterable<T> iterable, @Nullable Function<? super T, ? extends U> mapper,
        Supplier<V> collFactory, Collection<U> defaultIfNull) {
        return (iterable != null && mapper != null) ? stream(iterable).map(mapper).collect(Collectors.toCollection(collFactory)) : defaultIfNull;
    }

    public static <T> Collection<T> filter(@Nullable Iterable<T> iterable, @Nullable Predicate<T> predicate) {
        return filter(stream(iterable).collect(Collectors.toList()), predicate);
    }

    public static <T> Collection<T> filter(@Nullable Collection<T> coll, @Nullable Predicate<T> predicate) {
        return filter(coll, predicate, ArrayList::new);
    }

    public static <T, U extends Collection<T>> Collection<T> filter(@Nullable Collection<T> coll, @Nullable Predicate<T> predicate, Supplier<U> collFactory) {
        return filter(coll, predicate, collFactory, new ArrayList<>());
    }

    public static <T, U extends Collection<T>> Collection<T> filter(@Nullable Collection<T> coll, @Nullable Predicate<T> predicate, Supplier<U> collFactory,
        Collection<T> defaultIfNull) {
        return coll != null ? (predicate != null ? coll.stream().filter(predicate).collect(Collectors.toCollection(collFactory)) : coll.stream().collect(
            Collectors.toCollection(collFactory))) : defaultIfNull;
    }

    public static <T> T find(@Nullable Iterable<T> iterable) {
        return find(iterable, null);
    }

    public static <T> T find(@Nullable Iterable<T> iterable, @Nullable Predicate<T> predicate) {
        return iterable != null ? (predicate != null ? stream(iterable).filter(predicate).findFirst().orElse(null) : stream(iterable).findFirst().orElse(
            null)) : null;
    }

    public static <T> boolean exists(@Nullable Iterable<T> iterable, @Nullable Predicate<T> predicate) {
        return iterable != null && predicate != null && stream(iterable).anyMatch(predicate);
    }
}
