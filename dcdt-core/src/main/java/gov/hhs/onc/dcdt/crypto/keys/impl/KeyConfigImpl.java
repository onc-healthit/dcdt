package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyConfig;
import javax.annotation.Nullable;

public class KeyConfigImpl extends AbstractKeyDescriptor implements KeyConfig {
    @Override
    public void setKeyAlgorithm(@Nullable KeyAlgorithm keyAlg) {
        this.keyAlg = keyAlg;
    }

    @Override
    public void setKeySize(@Nullable Integer keySize) {
        this.keySize = keySize;
    }
}
