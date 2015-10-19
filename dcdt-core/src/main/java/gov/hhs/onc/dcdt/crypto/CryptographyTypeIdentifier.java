package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public interface CryptographyTypeIdentifier extends ToolIdentifier {
    public final static String PROP_NAME_TYPE = "type";

    public Class<?> getType();
}
