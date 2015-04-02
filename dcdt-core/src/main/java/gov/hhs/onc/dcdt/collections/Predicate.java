package gov.hhs.onc.dcdt.collections;

public interface Predicate<T> {
    public boolean test(T input) throws Exception;
}
