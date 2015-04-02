package gov.hhs.onc.dcdt.crypto;

public interface CryptographyTypeIdentifier extends CryptographyIdentifier {
    public Class<?> getType();
}
