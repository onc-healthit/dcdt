package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public interface CryptographyTaggedIdentifier extends ToolIdentifier {
    public int getTag();
}
