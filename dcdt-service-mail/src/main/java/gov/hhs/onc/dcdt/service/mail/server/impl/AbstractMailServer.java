package gov.hhs.onc.dcdt.service.mail.server.impl;

import gov.hhs.onc.dcdt.service.mail.config.MailServerConfig;
import gov.hhs.onc.dcdt.service.mail.server.MailServer;
import gov.hhs.onc.dcdt.service.mail.server.MailUserRepository;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolChannelServer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;
import javax.annotation.Resource;
import javax.mail.Session;

public abstract class AbstractMailServer<T extends MailServerConfig> extends AbstractToolChannelServer<T> implements MailServer<T> {
    protected abstract class AbstractMailServerChannelInitializer extends AbstractToolServerChannelInitializer {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            super.initChannel(channel);

            channel.attr(USER_REPO_ATTR_KEY).set(AbstractMailServer.this.userRepo);
        }
    }

    public final static AttributeKey<MailUserRepository> USER_REPO_ATTR_KEY = AttributeKey.valueOf("userRepo");

    @Resource(name = "mailUserRepoImpl")
    protected MailUserRepository userRepo;

    protected String mailSessionPlainBeanName;
    protected String mailSessionSslBeanName;

    protected AbstractMailServer(T config, String mailSessionPlainBeanName, String mailSessionSslBeanName) {
        super(config);

        this.mailSessionPlainBeanName = mailSessionPlainBeanName;
        this.mailSessionSslBeanName = mailSessionSslBeanName;
    }

    protected Session buildSession(boolean ssl) {
        return this.appContext.getBean((ssl ? this.mailSessionSslBeanName : this.mailSessionPlainBeanName), Session.class);
    }
}
