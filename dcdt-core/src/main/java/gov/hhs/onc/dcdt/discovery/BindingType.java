package gov.hhs.onc.dcdt.discovery;

import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import javax.annotation.Nullable;

public enum BindingType {
    NONE(null), ADDRESS(GeneralNameType.RFC822_NAME), DOMAIN(GeneralNameType.DNS_NAME);

    private final GeneralNameType nameType;

    private BindingType(@Nullable GeneralNameType nameType) {
        this.nameType = nameType;
    }

    public boolean isBound() {
        return (this.isAddressBound() || this.isDomainBound());
    }

    public boolean isAddressBound() {
        return (this == ADDRESS);
    }

    public boolean isDomainBound() {
        return (this == DOMAIN);
    }

    public boolean hasNameType() {
        return (this.nameType != null);
    }

    @Nullable
    public GeneralNameType getNameType() {
        return this.nameType;
    }
}
