package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class ToolIterableUtils {
    private static class WrappedElementIterator implements Iterator<Object[]> {
        private Iterator<?> iter;

        public WrappedElementIterator(Iterator<?> iter) {
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public Object[] next() {
            return ToolArrayUtils.wrapElements(iter.next());
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }

    private static class EmptyIterator<T> implements Iterator<T> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }

    private static class IteratorIterable<T> implements Iterable<T> {
        private Iterator<T> iter;

        public IteratorIterable(Iterator<T> iter) {
            this.iter = iter;
        }

        @Override
        public Iterator<T> iterator() {
            return this.iter;
        }
    }

    public static Iterator<Object[]> wrapElements(Iterator<?> iter) {
        return new WrappedElementIterator(iter);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T> List<T> asList(Iterable<T> iterable) {
        Class<?> iterableClass = iterable.getClass();

        if (List.class.isAssignableFrom(iterableClass)) {
            return (List<T>) iterable;
        }

        if (Collection.class.isAssignableFrom(iterableClass)) {
            return new ArrayList<>((Collection<T>) iterable);
        } else {
            List<T> list = new ArrayList<>();

            for (T elem : iterable) {
                list.add(elem);
            }

            return list;
        }
    }

    public static <T> Iterable<T> iterate(@Nullable Iterator<T> iter) {
        return new IteratorIterable<T>(iter);
    }
}
