package gov.hhs.onc.dcdt.dns;

public enum DnsResolverType {
    LOCAL("local"), EXTERNAL("external");

    private String type;

    private DnsResolverType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public String getType() {
        return this.type;
    }
}
