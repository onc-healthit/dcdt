package gov.hhs.onc.dcdt.crypto;

public interface CryptographyTypeIdentifier<T> extends CryptographyIdentifier {
    public Class<? extends T> getType();
}
