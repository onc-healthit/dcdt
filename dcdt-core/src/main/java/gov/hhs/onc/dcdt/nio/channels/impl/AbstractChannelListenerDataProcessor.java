package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerDataProcessor;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractChannelListenerDataProcessor extends AbstractToolBean implements ChannelListenerDataProcessor {
    protected InetProtocol protocol;
    protected byte[] reqData;

    protected AbstractChannelListenerDataProcessor(InetProtocol protocol, byte[] reqData) {
        this.protocol = protocol;
        this.reqData = reqData;
    }
}
