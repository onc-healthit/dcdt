package gov.hhs.onc.dcdt.crypto.keys;

import javax.annotation.Nullable;

public interface KeyPairConfig extends KeyPairDescriptor {
    public void setKeyAlgorithm(@Nullable KeyAlgorithm keyAlg);

    public void setKeySize(@Nullable Integer keySize);
}
