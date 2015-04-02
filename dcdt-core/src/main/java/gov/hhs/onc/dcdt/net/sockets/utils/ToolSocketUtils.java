package gov.hhs.onc.dcdt.net.sockets.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;

public abstract class ToolSocketUtils {
    public static DatagramPacket createPacket(int bufferLen) throws IOException {
        return new DatagramPacket(new byte[bufferLen], bufferLen);
    }

    public static DatagramPacket createPacket(byte[] buffer) throws IOException {
        return new DatagramPacket(buffer, buffer.length);
    }

    public static DatagramPacket createPacket(byte[] buffer, SocketAddress socketAddr) throws IOException {
        return new DatagramPacket(buffer, buffer.length, socketAddr);
    }
}
