package gov.hhs.onc.dcdt.net;

public enum InetProtocol {
    /**
     * Derived from the limits described in: <a href="http://tools.ietf.org/html/rfc1122#page-58">RFC 1122 - Requirements for Internet Hosts -- Communication
     * Layers (page 58)</a>
     */
    UDP("UDP", 512), TCP("TCP", Integer.MAX_VALUE);

    private final String protocol;
    private final int dataSizeMax;

    private InetProtocol(String protocol, int dataSizeMax) {
        this.protocol = protocol;
        this.dataSizeMax = dataSizeMax;
    }

    public int getDataSizeMax() {
        return this.dataSizeMax;
    }

    public String getProtocol() {
        return this.protocol;
    }
}
