package gov.hhs.onc.dcdt.http.lookup.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.dns.lookup.DnsNameService;
import gov.hhs.onc.dcdt.http.HttpTransportProtocol;
import gov.hhs.onc.dcdt.http.lookup.HttpLookupResult;
import gov.hhs.onc.dcdt.http.lookup.HttpLookupService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class HttpLookupServiceImpl extends AbstractToolBean implements HttpLookupService {
    private class HttpLookupClientResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
        private HttpLookupResult result;

        public HttpLookupClientResponseHandler(HttpLookupResult result) {
            this.result = result;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            if (cause instanceof ReadTimeoutException) {
                buildMessage(this.result, ToolMessageLevel.ERROR,
                    "HTTP %s lookup (reqUri=%s, remoteSocketAddr={%s}, reqHeaders=[%s]) read attempt timed out: %s", this.result.getRequestMethod().name(),
                    this.result.getRequestUri(), this.result.getRemoteSocketAddress(), StringUtils.join(this.result.getRequestHeaders(), "; "),
                    cause.getMessage());
            } else if (cause instanceof TooLongFrameException) {
                this.result.getMessages().add(
                    new ToolMessageImpl(ToolMessageLevel.ERROR, String.format("HTTP %s lookup (reqUri=%s, remoteSocketAddr={%s}) response was too large: %s",
                        this.result.getRequestMethod().name(), this.result.getRequestUri(), this.result.getRemoteSocketAddress(), cause.getMessage())));
            } else {
                buildMessage(this.result, ToolMessageLevel.ERROR,
                    "HTTP %s lookup (reqUri=%s, remoteSocketAddr={%s}, reqHeaders=[%s]) failed (respStatus={%s}, respHeaders=[%s]): %s", this.result
                        .getRequestMethod().name(), this.result.getRequestUri(), this.result.getRemoteSocketAddress(), StringUtils.join(
                        this.result.getRequestHeaders(), "; "), this.result.getResponseStatus(), StringUtils.join(this.result.getResponseHeaders(), "; "),
                    cause.getMessage());
            }

            context.close();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, FullHttpResponse resp) throws Exception {
            URI reqUri = this.result.getRequestUri();
            HttpMethod reqMethod = this.result.getRequestMethod();

            HttpResponseStatus respStatus = resp.getStatus();
            this.result.setResponseStatus(respStatus);

            HttpHeaders respHeaders = resp.headers();
            this.result.setResponseHeaders(respHeaders);

            if (respStatus.code() != HttpResponseStatus.OK.code()) {
                buildMessage(this.result, ToolMessageLevel.ERROR,
                    "HTTP %s lookup (reqUri=%s, remoteSocketAddr={%s}, reqHeaders=[%s]) failed (respStatus={%s}, respHeaders=[%s]).", reqMethod.name(), reqUri,
                    this.result.getRemoteSocketAddress(), StringUtils.join(this.result.getRequestHeaders(), "; "), respStatus,
                    StringUtils.join(respHeaders, "; "));

                return;
            }

            this.result.setResponseContent(resp.content().copy().array());

            context
                .close()
                .addListener(
                    closeFuture -> buildMessage(
                        this.result,
                        ToolMessageLevel.INFO,
                        "HTTP %s lookup (reqUri=%s, remoteSocketAddr={%s}, reqHeaders=[%s]) was successful (respStatus={%s}, respHeaders=[%s], respContentLen=%d).",
                        reqMethod.name(), reqUri, this.result.getRemoteSocketAddress(), StringUtils.join(this.result.getRequestHeaders(), "; "), respStatus,
                        StringUtils.join(respHeaders, "; "), ArrayUtils.getLength(this.result.getResponseContent())));
        }
    }

    private class HttpLookupClientChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            channel.config().setConnectTimeoutMillis(HttpLookupServiceImpl.this.connTimeout);

            ChannelPipeline channelPipeline = channel.pipeline();
            channelPipeline.addLast(new HttpRequestEncoder());
            channelPipeline.addLast(new HttpResponseDecoder());
            channelPipeline.addLast(new HttpContentDecompressor());
            channelPipeline.addLast(new HttpObjectAggregator(HttpLookupServiceImpl.this.maxContentLen));
            channelPipeline.addLast(new ReadTimeoutHandler(HttpLookupServiceImpl.this.readTimeout, TimeUnit.MILLISECONDS));
        }
    }

    private int connTimeout;
    private DnsNameService dnsNameService;
    private int maxContentLen;
    private int readTimeout;
    private ThreadPoolTaskExecutor taskExec;

    @Override
    public HttpLookupResult getUri(URI reqUri) {
        return this.lookupUri(reqUri, HttpMethod.GET);
    }

    @Override
    public HttpLookupResult lookupUri(URI reqUri, HttpMethod reqMethod) {
        HttpLookupResult result = new HttpLookupResultImpl(reqUri, reqMethod);
        String reqHost = reqUri.getHost();
        InetAddress remoteAddr;

        try {
            remoteAddr = this.dnsNameService.getByName(reqHost);
        } catch (UnknownHostException e) {
            buildMessage(result, ToolMessageLevel.ERROR, "Unable to resolve HTTP %s lookup URI (%s) host name to IP(v4) address.", reqMethod.name(), reqUri);

            return result;
        }

        int remotePort = reqUri.getPort();

        if (remotePort < 0) {
            remotePort = HttpTransportProtocol.HTTP.getDefaultPort();
        }

        InetSocketAddress remoteSocketAddr = new InetSocketAddress(remoteAddr, remotePort);
        result.setRemoteSocketAddress(remoteSocketAddr);

        EventLoopGroup clientEventLoopGroup = new NioEventLoopGroup(1, this.taskExec);
        ChannelFuture connFuture;

        try {
            try {
                connFuture =
                    new Bootstrap().group(clientEventLoopGroup).channel(NioSocketChannel.class).handler(new HttpLookupClientChannelInitializer())
                        .connect(remoteSocketAddr).await();
            } catch (InterruptedException e) {
                buildMessage(result, ToolMessageLevel.ERROR, "HTTP %s lookup (reqUri=%s) remote connection (socketAddr={%s}) attempt interrupted.",
                    reqMethod.name(), reqUri, remoteSocketAddr);

                return result;
            }

            if (connFuture.isCancelled()) {
                buildMessage(result, ToolMessageLevel.ERROR, "HTTP %s lookup (reqUri=%s) remote connection (socketAddr={%s}) attempt cancelled.",
                    reqMethod.name(), reqUri, result.getRemoteSocketAddress());

                return result;
            } else if (!connFuture.isSuccess()) {
                // noinspection ThrowableResultOfMethodCallIgnored
                Throwable connFailureCause = connFuture.cause();

                if (connFailureCause instanceof ConnectTimeoutException) {
                    buildMessage(result, ToolMessageLevel.ERROR, "HTTP %s lookup (reqUri=%s) remote connection (socketAddr={%s}) attempt timed out: %s",
                        reqMethod.name(), reqUri, result.getRemoteSocketAddress(), connFailureCause.getMessage());
                } else if (connFailureCause instanceof ConnectException) {
                    buildMessage(result, ToolMessageLevel.ERROR, "HTTP %s lookup (reqUri=%s) remote connection (socketAddr={%s}) attempt refused: %s",
                        reqMethod.name(), reqUri, result.getRemoteSocketAddress(), connFailureCause.getMessage());
                } else {
                    buildMessage(result, ToolMessageLevel.ERROR, "HTTP %s lookup (reqUri=%s) remote connection (socketAddr={%s}) attempt failed: %s",
                        reqMethod.name(), reqUri, result.getRemoteSocketAddress(), connFailureCause.getMessage());
                }

                return result;
            }

            Channel channel = connFuture.channel();
            channel.pipeline().addLast(new HttpLookupClientResponseHandler(result));

            channel.writeAndFlush(buildRequest(result, reqUri, reqMethod));

            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                buildMessage(result, ToolMessageLevel.ERROR, "HTTP %s lookup (reqUri=%s) remote connection (socketAddr={%s}) interrupted.", reqMethod.name(),
                    reqUri, remoteSocketAddr);
            }

            return result;
        } finally {
            clientEventLoopGroup.shutdownGracefully();
        }
    }

    private static FullHttpRequest buildRequest(HttpLookupResult result, URI reqUri, HttpMethod reqMethod) {
        FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, reqMethod, reqUri.toString());

        HttpHeaders reqHeaders = req.headers();
        reqHeaders.set(Names.HOST, reqUri.getHost());
        reqHeaders.set(Names.CONNECTION, Values.CLOSE);
        result.setRequestHeaders(reqHeaders);

        return req;
    }

    private static void buildMessage(HttpLookupResult result, ToolMessageLevel level, String msgFormat, Object ... msgArgs) {
        result.getMessages().add(new ToolMessageImpl(level, String.format(msgFormat, msgArgs)));
    }

    @Nonnegative
    @Override
    public int getConnectTimeout() {
        return this.connTimeout;
    }

    @Override
    public void setConnectTimeout(@Nonnegative int connTimeout) {
        this.connTimeout = connTimeout;
    }

    @Override
    public DnsNameService getDnsNameService() {
        return this.dnsNameService;
    }

    @Override
    public void setDnsNameService(DnsNameService dnsNameService) {
        this.dnsNameService = dnsNameService;
    }

    @Override
    public int getMaxContentLength() {
        return this.maxContentLen;
    }

    @Override
    public void setMaxContentLength(@Nonnegative int maxContentLen) {
        this.maxContentLen = maxContentLen;
    }

    @Nonnegative
    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(@Nonnegative int readTimeout) {
        this.readTimeout = readTimeout;
    }

    @Override
    public ThreadPoolTaskExecutor getTaskExecutor() {
        return this.taskExec;
    }

    @Override
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExec) {
        this.taskExec = taskExec;
    }
}
