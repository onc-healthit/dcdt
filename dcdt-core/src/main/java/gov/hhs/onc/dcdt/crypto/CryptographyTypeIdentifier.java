package gov.hhs.onc.dcdt.crypto;

public interface CryptographyTypeIdentifier extends CryptographyIdentifier {
    public final static String PROP_NAME_TYPE = "type";

    public Class<?> getType();
}
