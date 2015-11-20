package gov.hhs.onc.dcdt.service.mail.server.impl;

import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolChannelServer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;
import javax.annotation.Resource;
import javax.mail.Session;

public abstract class AbstractMailServer<T extends Enum<T> & TransportProtocol, U extends MailServerConfig<T>> extends AbstractToolChannelServer<T, U>
    implements MailServer<T, U> {
    protected abstract class AbstractMailServerChannelInitializer extends AbstractToolServerChannelInitializer {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            super.initChannel(channel);

            channel.attr(GATEWAY_ATTR_KEY).set(AbstractMailServer.this.gateway);
        }
    }

    public final static AttributeKey<MailGateway> GATEWAY_ATTR_KEY = AttributeKey.valueOf("gateway");

    @Resource(name = "mailGatewayImpl")
    protected MailGateway gateway;

    @Resource(name = "mailSessionDefault")
    protected Session mailSession;

    protected AbstractMailServer(U config) {
        super(config);
    }
}
