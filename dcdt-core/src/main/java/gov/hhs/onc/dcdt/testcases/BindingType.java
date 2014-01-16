package gov.hhs.onc.dcdt.testcases;

public enum BindingType {
    NONE("none"), ANY("any"), ADDRESS("address"), DOMAIN("domain");

    private final String binding;

    private BindingType(String binding) {
        this.binding = binding;
    }

    public String getBinding() {
        return this.binding;
    }

    public boolean isAddressBound() {
        return (this == ANY) || (this == ADDRESS);
    }

    public boolean isDomainBound() {
        return (this == ANY) || (this == DOMAIN);
    }
}
