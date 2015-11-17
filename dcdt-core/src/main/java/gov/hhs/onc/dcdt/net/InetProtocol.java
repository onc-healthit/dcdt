package gov.hhs.onc.dcdt.net;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum InetProtocol implements ToolIdentifier {
    /**
     * Derived from the limits described in: <a href="http://tools.ietf.org/html/rfc1122#page-58">RFC 1122 - Requirements for Internet Hosts -- Communication
     * Layers (page 58)</a>
     */
    UDP(512), TCP(Integer.MAX_VALUE);

    private final int dataSizeMax;

    private InetProtocol(int dataSizeMax) {
        this.dataSizeMax = dataSizeMax;
    }

    public int getDataSizeMax() {
        return this.dataSizeMax;
    }

    @Override
    public String getId() {
        return this.name();
    }
}
