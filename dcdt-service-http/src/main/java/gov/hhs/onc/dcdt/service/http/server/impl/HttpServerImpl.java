package gov.hhs.onc.dcdt.service.http.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 1)
public class HttpServerImpl extends AbstractToolLifecycleBean implements HttpServer {
    private class HttpServiceRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            // TEMP: dev
            LOGGER.error(String.format("exceptionCaught"), cause);

            context.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR)).addListener(
                ChannelFutureListener.CLOSE);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, FullHttpRequest req) throws Exception {
            // TEMP: dev
            LOGGER.info(String.format("channelRead0={%s}", req));

            context.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
            // TEMP: dev
            LOGGER.warn(String.format("channelRead={%s}", msg));

            super.channelRead(context, msg);
        }
    }

    private class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            channel.config().setConnectTimeoutMillis(HttpServerImpl.this.config.getConnectTimeout());

            ChannelPipeline channelPipeline = channel.pipeline();
            channelPipeline.addLast(new HttpRequestDecoder());
            channelPipeline.addLast(new HttpResponseEncoder());
            channelPipeline.addLast(new HttpObjectAggregator(HttpServerImpl.this.config.getMaxContentLength()));
            channelPipeline.addLast(new ReadTimeoutHandler(HttpServerImpl.this.config.getReadTimeout(), TimeUnit.MILLISECONDS));
            channelPipeline.addLast(new WriteTimeoutHandler(HttpServerImpl.this.config.getWriteTimeout(), TimeUnit.MILLISECONDS));
            channelPipeline.addLast(new HttpServiceRequestHandler());
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerImpl.class);

    @Resource(name = "taskExecServiceHttpServerReq")
    private ThreadPoolTaskExecutor reqTaskExec;

    private HttpServerConfig config;
    private Channel serverChannel;

    public HttpServerImpl(HttpServerConfig config) {
        this.config = config;
    }

    @Override
    public boolean isRunning() {
        return (super.isRunning() && (this.serverChannel != null) && this.serverChannel.isActive());
    }

    @Override
    protected void stopInternal() throws Exception {
        this.serverChannel.close().sync();

        LOGGER.info(String.format("Stopped HTTP server (class=%s, name=%s, host=%s, port=%d).", ToolClassUtils.getName(this), this.config.getName(),
            this.config.getHost(), this.config.getPort()));
    }

    @Override
    protected void startInternal() throws Exception {
        EventLoopGroup acceptorEventLoopGroup = new NioEventLoopGroup(1), workerEventLoopGroup = new NioEventLoopGroup();

        try {
            // noinspection ConstantConditions
            this.serverChannel =
                new ServerBootstrap().option(ChannelOption.SO_REUSEADDR, true).group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class).childHandler(new HttpServerChannelInitializer()).bind(this.config.toSocketAddress()).sync()
                    .channel();

            LOGGER.info(String.format("Started HTTP server (class=%s, name=%s, host=%s, port=%d).", ToolClassUtils.getName(this), this.config.getName(),
                this.config.getHost(), this.config.getPort()));

            this.serverChannel.closeFuture().addListener(future -> {
                acceptorEventLoopGroup.shutdownGracefully();
                workerEventLoopGroup.shutdownGracefully();
            });
        } catch (Exception e) {
            acceptorEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();

            throw e;
        }
    }

    @Override
    public HttpServerConfig getConfig() {
        return this.config;
    }

    @Override
    @Resource(name = "taskExecServiceHttpServer")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
