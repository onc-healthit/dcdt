package gov.hhs.onc.dcdt.net;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import javax.annotation.Nonnegative;

public interface TransportProtocol extends ToolIdentifier {
    @Nonnegative
    public int getDefaultPort();

    public String getScheme();

    public SslType getSslType();
}
