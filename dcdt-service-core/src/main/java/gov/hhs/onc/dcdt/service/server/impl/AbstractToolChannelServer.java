package gov.hhs.onc.dcdt.service.server.impl;

import gov.hhs.onc.dcdt.service.ToolServiceException;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolChannelServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractToolChannelServer<T extends ToolServerConfig> extends AbstractToolServer<T> implements ToolChannelServer<T> {
    protected abstract class AbstractToolServerRequestHandler<U> extends SimpleChannelInboundHandler<U> {
        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            String protocol = AbstractToolChannelServer.this.config.getProtocol();
            SocketChannel channel = ((SocketChannel) context.channel());
            SocketAddress bindSocketAddr = AbstractToolChannelServer.this.config.toSocketAddress(), localSocketAddr = channel.localAddress(), remoteSocketAddr =
                channel.remoteAddress();

            if (cause instanceof ConnectTimeoutException) {
                throw new ToolServiceException(String.format(
                    "%s server (bindSocketAddr={%s}) connection (localSocketAddr={%s}, remoteSocketAddr={%s}) attempt timed out.", protocol, bindSocketAddr,
                    localSocketAddr, remoteSocketAddr), cause);
            } else if (cause instanceof ReadTimeoutException) {
                throw new ToolServiceException(String.format(
                    "%s server (bindSocketAddr={%s}) connection (localSocketAddr={%s}, remoteSocketAddr={%s}) read attempt timed out.", protocol,
                    bindSocketAddr, localSocketAddr, remoteSocketAddr), cause);
            } else if (cause instanceof TooLongFrameException) {
                throw new ToolServiceException(String.format(
                    "%s server (bindSocketAddr={%s}) connection (localSocketAddr={%s}, remoteSocketAddr={%s}) response was too large.", protocol,
                    bindSocketAddr, localSocketAddr, remoteSocketAddr), cause);
            } else if (cause instanceof ConnectException) {
                throw new ToolServiceException(String.format(
                    "%s server (bindSocketAddr={%s}) connection (localSocketAddr={%s}, remoteSocketAddr={%s}) attempt refused.", protocol, bindSocketAddr,
                    localSocketAddr, remoteSocketAddr), cause);
            }

            throw ((Exception) cause);
        }
    }

    protected abstract class AbstractToolServerChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            channel.config().setConnectTimeoutMillis(AbstractToolChannelServer.this.config.getConnectTimeout());

            channel.attr(CONFIG_ATTR_KEY).set(AbstractToolChannelServer.this.config);
        }
    }

    public final static AttributeKey<ToolServerConfig> CONFIG_ATTR_KEY = AttributeKey.valueOf("config");

    protected EventLoopGroup acceptorEventLoopGroup;
    protected EventLoopGroup workerEventLoopGroup;
    protected Channel serverChannel;

    protected AbstractToolChannelServer(T config) {
        super(config);
    }

    @Override
    public boolean isRunning() {
        return (super.isRunning() && ((this.acceptorEventLoopGroup != null) || (this.workerEventLoopGroup != null) || (this.serverChannel != null)));
    }

    @Override
    protected void stopInternal() throws Exception {
        this.stopChannel();

        super.stopInternal();
    }

    protected void stopChannel() throws InterruptedException {
        CountDownLatch shutdownLatch = new CountDownLatch(4);

        if ((this.serverChannel != null) && this.serverChannel.isActive()) {
            this.serverChannel.close().addListener(serverChannelCloseFuture -> shutdownLatch.countDown());
        } else {
            shutdownLatch.countDown();
        }

        if (this.acceptorEventLoopGroup != null) {
            this.acceptorEventLoopGroup.shutdownGracefully().addListener(eventLoopGroupShutdownFuture -> shutdownLatch.countDown());
        } else {
            shutdownLatch.countDown();
        }

        if (this.workerEventLoopGroup != null) {
            this.workerEventLoopGroup.shutdownGracefully().addListener(eventLoopGroupShutdownFuture -> shutdownLatch.countDown());
        } else {
            shutdownLatch.countDown();
        }

        shutdownLatch.await(5, TimeUnit.SECONDS);
    }

    @Override
    protected void startInternal() throws Exception {
        this.acceptorEventLoopGroup = new NioEventLoopGroup(1, this.reqTaskExec);
        this.workerEventLoopGroup = new NioEventLoopGroup(this.reqTaskExec.getMaxPoolSize(), this.reqTaskExec);

        try {
            // noinspection ConstantConditions
            this.serverChannel =
                this.initializeBootstrap(
                    new ServerBootstrap().option(ChannelOption.SO_REUSEADDR, true).group(this.acceptorEventLoopGroup, this.workerEventLoopGroup)
                        .channel(NioServerSocketChannel.class)).bind(this.config.toSocketAddress()).sync().channel();

            super.startInternal();
        } catch (Exception e) {
            this.stopChannel();

            throw e;
        }
    }

    protected ServerBootstrap initializeBootstrap(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, this.config.getBacklog());

        return bootstrap;
    }
}
