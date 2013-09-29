package gov.hhs.onc.dcdt.service.dns;


import gov.hhs.onc.dcdt.service.dns.conf.DnsServerConfig;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Message;

public class DnsUdpSocketServer extends DnsSocketServer<DatagramSocket, DatagramSocket, DatagramPacket> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DnsUdpSocketServer.class);

    public DnsUdpSocketServer() {
    }

    public DnsUdpSocketServer(DnsServerConfig dnsServerConfig) {
        super(dnsServerConfig);
    }

    @Override
    public void start() {
        try {
            while(!this.socketServer.isClosed()) {
                byte[] inMsgData = new byte[this.socketServer.getReceiveBufferSize()];
                DatagramPacket inMsgPacket = new DatagramPacket(inMsgData, inMsgData.length);

                this.socketServer.receive(inMsgPacket);

                this.processMessage(this.socketServer, inMsgPacket, inMsgData);
            }
        } catch (Throwable th) {
            LOGGER.error("Unable to start DNS UDP socket server.", th);
        }
    }

    @Override
    public void stop() {
        try {
            this.socketServer.close();
        } catch (Throwable th) {
            LOGGER.error("Unable to stop DNS UDP socket server.", th);
        }
    }

    @Override
    protected void sendMessage(DatagramSocket socket, DatagramPacket inMsgObj, Message inMsg, Message outMsg) throws IOException
    {
        byte[] outMsgData = outMsg.toWire();
        
        socket.send(new DatagramPacket(outMsgData, outMsgData.length, inMsgObj.getSocketAddress()));
    }

    @Override
    protected DatagramSocket createSocketServer() {
        try {
            return new DatagramSocket(this.dnsServerConfig.getPort(), Inet4Address.getByName(this.dnsServerConfig.getHost()));
        } catch (Throwable th) {
            LOGGER.error("Unable to create DNS UDP socket server.", th);
        }

        return null;
    }
}
