package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairConfig;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import javax.annotation.Nullable;

public class KeyPairConfigImpl extends AbstractCryptographyDescriptor implements KeyPairConfig {
    private KeyAlgorithm keyAlg;
    private Integer keySize;

    @Override
    public boolean hasKeyAlgorithm() {
        return this.keyAlg != null;
    }

    @Nullable
    @Override
    public KeyAlgorithm getKeyAlgorithm() {
        return this.keyAlg;
    }

    @Override
    public void setKeyAlgorithm(@Nullable KeyAlgorithm keyAlg) {
        this.keyAlg = keyAlg;
    }

    @Override
    public boolean hasKeySize() {
        return ToolNumberUtils.isPositive(this.keySize);
    }

    @Nullable
    @Override
    public Integer getKeySize() {
        return this.keySize;
    }

    @Override
    public void setKeySize(@Nullable Integer keySize) {
        this.keySize = keySize;
    }
}
