package gov.hhs.onc.dcdt.service.ldap.config.impl;

import org.apache.directory.server.annotations.TransportType;

/**
 * @see org.apache.directory.server.annotations.CreateTransport
 */
public class TransportConfig {
    private String protocol;
    private TransportType type = TransportType.TCP;
    private int port = -1;
    private String address = "localhost";
    private int backlog = 50;
    private boolean ssl;
    private int nbThreads = 3;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public int getNbThreads() {
        return this.nbThreads;
    }

    public void setNbThreads(int nbThreads) {
        this.nbThreads = nbThreads;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public TransportType getType() {
        return this.type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }
}
