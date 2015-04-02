package gov.hhs.onc.dcdt.collections;

public interface Function<T, U> {
    public U apply(T input) throws Exception;
}
