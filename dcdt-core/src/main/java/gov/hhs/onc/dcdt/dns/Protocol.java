package gov.hhs.onc.dcdt.dns;

public enum Protocol {

    TCP("_tcp"), UDP("_udp");

    private String protocol;

    private Protocol(final String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return this.protocol;
    }

}
