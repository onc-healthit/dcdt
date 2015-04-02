package gov.hhs.onc.dcdt.discovery;

public enum BindingType {
    NONE, ADDRESS, DOMAIN;

    public boolean isBound() {
        return (this.isAddressBound() || this.isDomainBound());
    }

    public boolean isAddressBound() {
        return (this == ADDRESS);
    }

    public boolean isDomainBound() {
        return (this == DOMAIN);
    }
}
