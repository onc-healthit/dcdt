package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import javax.annotation.Nullable;

public interface KeyDescriptor extends CryptographyDescriptor {
    public boolean hasKeyAlgorithm();

    @Nullable
    public KeyAlgorithm getKeyAlgorithm();

    public boolean hasKeySize();

    @Nullable
    public Integer getKeySize();
}
