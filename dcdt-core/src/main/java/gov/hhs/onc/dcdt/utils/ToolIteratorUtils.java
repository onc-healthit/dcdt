package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.IteratorUtils;

public abstract class ToolIteratorUtils {
    public static <T> Iterator<T> chainedArrayIterator(@Nullable Iterable<? extends T[]> arrs) {
        return IteratorUtils.chainedIterator(getArrayIterators(arrs));
    }

    public static <T> Iterator<T> chainedIterator(@Nullable Iterable<? extends Iterable<? extends T>> iterables) {
        return IteratorUtils.chainedIterator(getIterators(iterables));
    }

    public static <T> List<Iterator<? extends T>> getArrayIterators(@Nullable Iterable<? extends T[]> arrs) {
        List<Iterator<? extends T>> iterators = new ArrayList<>();
        ToolStreamUtils.stream(arrs).filter(Objects::nonNull).forEach(arr -> iterators.add(IteratorUtils.arrayIterator(arr)));

        return iterators;
    }

    public static <T> List<Iterator<? extends T>> getIterators(@Nullable Iterable<? extends Iterable<? extends T>> iterables) {
        List<Iterator<? extends T>> iterators = new ArrayList<>();
        ToolStreamUtils.stream(iterables).filter(Objects::nonNull).forEach(iterable -> iterators.add(iterable.iterator()));

        return iterators;
    }
}
