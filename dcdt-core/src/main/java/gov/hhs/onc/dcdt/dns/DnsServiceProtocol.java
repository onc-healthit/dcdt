package gov.hhs.onc.dcdt.dns;

public enum DnsServiceProtocol {
    TCP("_tcp"), UDP("_udp");

    private final String serviceProtocol;

    private DnsServiceProtocol(String serviceProtocol) {
        this.serviceProtocol = serviceProtocol;
    }

    @Override
    public String toString() {
        return this.serviceProtocol;
    }

    public String getServiceProtocol() {
        return this.serviceProtocol;
    }
}
