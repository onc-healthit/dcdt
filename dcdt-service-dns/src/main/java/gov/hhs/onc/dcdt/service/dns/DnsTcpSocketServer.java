package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.service.dns.conf.DnsServerConfig;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Message;

public class DnsTcpSocketServer extends DnsSocketServer<ServerSocket, Socket, Void> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DnsTcpSocketServer.class);

    public DnsTcpSocketServer() {
    }

    public DnsTcpSocketServer(DnsServerConfig dnsServerConfig) {
        super(dnsServerConfig);
    }

    @Override
    public void start() {
        try {
            while (!this.socketServer.isClosed()) {
                Socket socket = this.socketServer.accept();
                InputStream inStream = socket.getInputStream();

                try (DataInputStream inDataStream = new DataInputStream(inStream)) {
                    this.processMessage(socket, null, IOUtils.toByteArray(inDataStream));
                }
            }
        } catch (Throwable th) {
            LOGGER.error("Unable to start DNS TCP socket server.", th);
        }
    }

    @Override
    public void stop() {
        try {
            this.socketServer.close();
        } catch (Throwable th) {
            LOGGER.error("Unable to stop DNS TCP socket server.", th);
        }
    }

    @Override
    protected void sendMessage(Socket socket, Void inMsgObj, Message inMsg, Message outMsg) throws IOException {
        try (DataOutputStream outDataStream = new DataOutputStream(socket.getOutputStream())) {
            outDataStream.write(outMsg.toWire());
            outDataStream.flush();
        }
    }

    @Override
    protected ServerSocket createSocketServer() {
        try {
            return new ServerSocket(this.dnsServerConfig.getPort(), -1, Inet4Address.getByName(this.dnsServerConfig.getHost()));
        } catch (Throwable th) {
            LOGGER.error("Unable to create DNS TCP socket server.", th);
        }

        return null;
    }
}
