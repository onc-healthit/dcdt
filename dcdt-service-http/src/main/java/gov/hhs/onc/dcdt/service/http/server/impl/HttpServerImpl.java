package gov.hhs.onc.dcdt.service.http.server.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.crl.CrlConfig;
import gov.hhs.onc.dcdt.crypto.crl.CrlGenerator;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils;
import gov.hhs.onc.dcdt.net.utils.ToolUriUtils;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import gov.hhs.onc.dcdt.service.server.impl.AbstractToolChannelServer;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialType;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeType;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 2)
public class HttpServerImpl extends AbstractToolChannelServer<HttpServerConfig> implements HttpServer {
    private class HttpServerRequestHandler extends AbstractToolServerRequestHandler<FullHttpRequest> {
        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            try {
                super.exceptionCaught(context, cause);
            } catch (Exception e) {
                HttpServerRequestDecoder reqDecoder = context.pipeline().get(HttpServerRequestDecoder.class);

                LOGGER.error(
                    String.format("Unable to process HTTP server (host={%s}, port=%d) request (version=%s, method=%s, uri=%s).",
                        HttpServerImpl.this.config.getHost(), HttpServerImpl.this.config.getPort(), reqDecoder.getRequestVersion(),
                        reqDecoder.getRequestMethod(), reqDecoder.getRequestUri()), e);

                this.writeResponse(context, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, FullHttpRequest req) throws Exception {
            if (req.getMethod() != HttpMethod.GET) {
                this.writeResponse(context, HttpResponseStatus.METHOD_NOT_ALLOWED);

                return;
            }

            URI reqUri = ToolUriUtils.fromString(req.getUri());
            String reqPath = reqUri.getPath();
            HttpServerRequestDecoder reqDecoder = context.pipeline().get(HttpServerRequestDecoder.class);

            if (HttpServerImpl.this.discoveryTestcaseIssuerCredCertPaths.containsKey(reqPath)) {
                // noinspection ConstantConditions
                CertificateInfo discoveryTestcaseIssuerCredCertInfo =
                    HttpServerImpl.this.discoveryTestcaseIssuerCredCertPaths.get(reqPath).getCredentialInfo().getCertificateDescriptor();
                // noinspection ConstantConditions
                CertificateType discoveryTestcaseIssuerCredCertType = discoveryTestcaseIssuerCredCertInfo.getCertificateType();

                // noinspection ConstantConditions
                this.writeResponse(context, discoveryTestcaseIssuerCredCertType.getContentType(),
                    CertificateUtils.writeCertificate(discoveryTestcaseIssuerCredCertInfo.getCertificate(), DataEncoding.DER));

                LOGGER
                    .info(String
                        .format(
                            "Processed HTTP server (host={%s}, port=%d) issuer certificate (type=%s, subjDn={%s}, serialNum=%s, issuerDn={%s}) request (version=%s, method=%s, uri=%s).",
                            HttpServerImpl.this.config.getHost(), HttpServerImpl.this.config.getPort(), discoveryTestcaseIssuerCredCertType.name(),
                            discoveryTestcaseIssuerCredCertInfo.getSubjectDn(), discoveryTestcaseIssuerCredCertInfo.getSerialNumber(),
                            discoveryTestcaseIssuerCredCertInfo.getIssuerDn(), reqDecoder.getRequestVersion(), reqDecoder.getRequestMethod(),
                            reqDecoder.getRequestUri()));
            } else if (HttpServerImpl.this.discoveryTestcaseIssuerCredCrlPaths.containsKey(reqPath)) {
                DiscoveryTestcaseCredential discoveryTestcaseCred = HttpServerImpl.this.discoveryTestcaseIssuerCredCrlPaths.get(reqPath);
                CrlConfig discoveryTestcaseIssuerCredCrlConfig = discoveryTestcaseCred.getCrlConfig();

                // noinspection ConstantConditions
                this.writeResponse(
                    context,
                    discoveryTestcaseIssuerCredCrlConfig.getCrlType().getContentType(),
                    CrlUtils.writeCrl(
                        HttpServerImpl.this.crlGen.generateCrl(discoveryTestcaseCred.getCredentialInfo().getKeyDescriptor().getPrivateKeyInfo(),
                            discoveryTestcaseIssuerCredCrlConfig).getCrl(), DataEncoding.DER));

                LOGGER.info(String.format("Processed HTTP server (host={%s}, port=%d) CRL (issuerDn={%s}) request (version=%s, method=%s, uri=%s).",
                    HttpServerImpl.this.config.getHost(), HttpServerImpl.this.config.getPort(), discoveryTestcaseIssuerCredCrlConfig.getIssuerDn(),
                    reqDecoder.getRequestVersion(), reqDecoder.getRequestMethod(), reqDecoder.getRequestUri()));
            } else {
                this.writeResponse(context, HttpResponseStatus.NOT_FOUND);
            }
        }

        private void writeResponse(ChannelHandlerContext context, HttpResponseStatus respStatus) {
            this.writeResponse(context, respStatus, null);
        }

        private void writeResponse(ChannelHandlerContext context, MimeType respContentType, byte ... respContent) {
            this.writeResponse(context, HttpResponseStatus.OK, respContentType, respContent);
        }

        private void writeResponse(ChannelHandlerContext context, HttpResponseStatus respStatus, @Nullable MimeType respContentType, byte ... respContent) {
            int respContentLen = respContent.length;
            FullHttpResponse resp =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, respStatus,
                    ((respContentLen == 0) ? Unpooled.buffer(0) : Unpooled.wrappedBuffer(respContent)));

            HttpHeaders.setContentLength(resp, respContentLen);

            if (respContentType != null) {
                resp.headers().set(Names.CONTENT_TYPE, respContentType.toString());
            }

            context.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private class HttpServerRequestDecoder extends HttpRequestDecoder {
        private String reqMethod;
        private String reqUri;
        private String reqVersion;

        @Override
        protected HttpMessage createMessage(String[] initialLine) throws Exception {
            if (initialLine.length >= 1) {
                this.reqMethod = initialLine[0];

                if (initialLine.length >= 2) {
                    this.reqUri = initialLine[1];

                    if (initialLine.length >= 3) {
                        this.reqVersion = initialLine[2];
                    }
                }
            }

            return super.createMessage(initialLine);
        }

        @Nullable
        public String getRequestMethod() {
            return this.reqMethod;
        }

        @Nullable
        public String getRequestUri() {
            return this.reqUri;
        }

        @Nullable
        public String getRequestVersion() {
            return this.reqVersion;
        }
    }

    private class HttpServerChannelInitializer extends AbstractToolServerChannelInitializer {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            super.initChannel(channel);

            ChannelPipeline channelPipeline = channel.pipeline();
            channelPipeline.addLast(new HttpServerRequestDecoder());
            channelPipeline.addLast(new HttpResponseEncoder());
            channelPipeline.addLast(new HttpObjectAggregator(HttpServerImpl.this.config.getMaxContentLength()));
            channelPipeline.addLast(new ReadTimeoutHandler(HttpServerImpl.this.config.getReadTimeout(), TimeUnit.MILLISECONDS));
            channelPipeline.addLast(new WriteTimeoutHandler(HttpServerImpl.this.config.getWriteTimeout(), TimeUnit.MILLISECONDS));
            channelPipeline.addLast(new HttpServerRequestHandler());
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerImpl.class);

    @Resource(name = "crlGenImpl")
    private CrlGenerator crlGen;

    private Map<String, DiscoveryTestcaseCredential> discoveryTestcaseIssuerCredCertPaths;
    private Map<String, DiscoveryTestcaseCredential> discoveryTestcaseIssuerCredCrlPaths;

    public HttpServerImpl(HttpServerConfig config) {
        super(config);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // noinspection ConstantConditions
        Map<String, DiscoveryTestcaseCredential> discoveryTestcaseIssuerCreds =
            ToolBeanFactoryUtils
                .getBeansOfType(this.appContext, DiscoveryTestcaseCredential.class)
                .stream()
                .filter(
                    discoveryTestcaseCred -> ((discoveryTestcaseCred.getType() == DiscoveryTestcaseCredentialType.CA) && discoveryTestcaseCred.hasCrlConfig() && discoveryTestcaseCred
                        .hasCredentialInfo()))
                .collect(ToolStreamUtils.toMap(ToolNamedBean::getName, Function.<DiscoveryTestcaseCredential>identity(), HashMap::new));
        int numDiscoveryTestcaseIssuerCreds = discoveryTestcaseIssuerCreds.size();

        this.discoveryTestcaseIssuerCredCertPaths = new HashMap<>(numDiscoveryTestcaseIssuerCreds);
        this.discoveryTestcaseIssuerCredCrlPaths = new HashMap<>(numDiscoveryTestcaseIssuerCreds);

        discoveryTestcaseIssuerCreds.forEach((discoveryTestcaseIssuerCredName, discoveryTestcaseIssuerCred) -> {
            // noinspection ConstantConditions
            String discoveryTestcaseIssuerCredCertPath =
                (ToolUriUtils.PATH_DELIM + discoveryTestcaseIssuerCredName + FilenameUtils.EXTENSION_SEPARATOR + discoveryTestcaseIssuerCred
                    .getCredentialConfig().getCertificateDescriptor().getCertificateType().getFileNameExtension()), discoveryTestcaseIssuerCredCrlPath =
                (ToolUriUtils.PATH_DELIM + discoveryTestcaseIssuerCredName + FilenameUtils.EXTENSION_SEPARATOR + discoveryTestcaseIssuerCred.getCrlConfig()
                    .getCrlType().getFileNameExtension());

            this.discoveryTestcaseIssuerCredCertPaths.put(discoveryTestcaseIssuerCredCertPath, discoveryTestcaseIssuerCred);
            this.discoveryTestcaseIssuerCredCrlPaths.put(discoveryTestcaseIssuerCredCrlPath, discoveryTestcaseIssuerCred);

            LOGGER.debug(String.format("HTTP server (host={%s}, port=%d) Discovery testcase issuer credential (name=%s) hosted: certPath=%s, crlPath=%s",
                this.config.getHost(), this.config.getPort(), discoveryTestcaseIssuerCredName, discoveryTestcaseIssuerCredCertPath,
                discoveryTestcaseIssuerCredCrlPath));
        });
    }

    @Override
    protected ServerBootstrap initializeBootstrap(ServerBootstrap bootstrap) {
        return super.initializeBootstrap(bootstrap).childHandler(new HttpServerChannelInitializer());
    }

    @Override
    public boolean hasDiscoveryTestcaseIssuerCredentialCertificatePaths() {
        return !MapUtils.isEmpty(this.discoveryTestcaseIssuerCredCertPaths);
    }

    @Nullable
    @Override
    public Map<String, DiscoveryTestcaseCredential> getDiscoveryTestcaseIssuerCredentialCertificatePaths() {
        return this.discoveryTestcaseIssuerCredCertPaths;
    }

    @Override
    public boolean hasDiscoveryTestcaseIssuerCredentialCrlPaths() {
        return !MapUtils.isEmpty(this.discoveryTestcaseIssuerCredCrlPaths);
    }

    @Nullable
    @Override
    public Map<String, DiscoveryTestcaseCredential> getDiscoveryTestcaseIssuerCredentialCrlPaths() {
        return this.discoveryTestcaseIssuerCredCrlPaths;
    }
}
