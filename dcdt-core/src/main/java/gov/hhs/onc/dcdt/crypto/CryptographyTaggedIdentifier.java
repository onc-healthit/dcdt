package gov.hhs.onc.dcdt.crypto;

public interface CryptographyTaggedIdentifier extends CryptographyIdentifier {
    public final static String PROP_NAME_TAG = "tag";

    public int getTag();
}
