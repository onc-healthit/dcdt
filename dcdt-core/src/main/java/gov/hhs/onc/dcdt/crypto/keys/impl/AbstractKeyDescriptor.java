package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyDescriptor;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractKeyDescriptor extends AbstractCryptographyDescriptor implements KeyDescriptor {
    protected KeyAlgorithm keyAlg;
    protected Integer keySize;

    @Override
    protected void reset() {
        this.keyAlg = null;
        this.keySize = null;
    }

    @Override
    public boolean hasKeyAlgorithm() {
        return (this.keyAlg != null);
    }

    @Nullable
    @Override
    @Transient
    public KeyAlgorithm getKeyAlgorithm() {
        return this.keyAlg;
    }

    @Override
    public boolean hasKeySize() {
        return ToolNumberUtils.isPositive(this.keySize);
    }

    @Nullable
    @Override
    @Transient
    public Integer getKeySize() {
        return this.keySize;
    }
}
