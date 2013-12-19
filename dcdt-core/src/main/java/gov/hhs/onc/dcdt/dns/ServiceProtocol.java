package gov.hhs.onc.dcdt.dns;

public enum ServiceProtocol {

    TCP("_tcp"), UDP("_udp");

    private String serviceProtocol;

    private ServiceProtocol(final String serviceProtocol) {
        this.serviceProtocol = serviceProtocol;
    }

    public String getServiceProtocol() {
        return this.serviceProtocol;
    }

}
