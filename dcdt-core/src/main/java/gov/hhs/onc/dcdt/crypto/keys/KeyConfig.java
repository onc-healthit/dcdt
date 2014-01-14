package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import gov.hhs.onc.dcdt.crypto.CryptographyConfig.GenerateConstraintGroup;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@ValidKeyConfig(groups = { GenerateConstraintGroup.class })
public interface KeyConfig extends KeyDescriptor, CryptographyConfig {
    @NotNull(message = "{dcdt.validation.constraints.crypto.keys.key.alg.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public KeyAlgorithm getKeyAlgorithm();

    @NotNull(message = "{dcdt.validation.constraints.crypto.keys.key.size.NotNull.msg}", groups = { GenerateConstraintGroup.class })
    @Nullable
    @Override
    public Integer getKeySize();

    public void setKeyAlgorithm(@Nullable KeyAlgorithm keyAlg);

    public void setKeySize(@Nullable Integer keySize);
}
