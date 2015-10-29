package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public interface CryptographyTypeIdentifier extends ToolIdentifier {
    public Class<?> getType();
}
