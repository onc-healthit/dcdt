package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.server.impl.AbstractMailServer;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpReplyParameters;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServer;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.AuthCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.DataCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.EhloCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.HeloCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.MailCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.QuitCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.RcptCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.RsetCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.StartTlsCommand;
import gov.hhs.onc.dcdt.service.mail.smtp.command.impl.VrfyCommand;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingService;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmtpServerImpl extends AbstractMailServer<SmtpServerConfig> implements SmtpServer {
    private class SmtpServerRequestDecoder extends DelimiterBasedFrameDecoder {
        private boolean decodeData;
        private CompositeByteBuf reqBuffer;
        private String req;

        public SmtpServerRequestDecoder(boolean decodeData) {
            super((decodeData ? SmtpServerImpl.this.config.getMaxDataFrameLength() : SmtpServerImpl.this.config.getMaxCommandFrameLength()), (decodeData
                ? DATA_DELIM_BUFFER : CMD_DELIM_BUFFER));

            this.setSingleDecode((this.decodeData = decodeData));
        }

        @Override
        protected void decodeLast(ChannelHandlerContext context, ByteBuf decodeBuffer, List<Object> reqObjs) throws Exception {
            super.decodeLast(context, decodeBuffer, reqObjs);

            if (this.reqBuffer != null) {
                this.req = this.reqBuffer.toString(CharsetUtil.US_ASCII);
                this.reqBuffer.clear();
            }
        }

        @Nullable
        @Override
        protected Object decode(ChannelHandlerContext context, ByteBuf decodeBuffer) throws Exception {
            this.req = null;

            if ((decodeBuffer = ((ByteBuf) super.decode(context, decodeBuffer))) != null) {
                ((this.reqBuffer != null) ? this.reqBuffer : (this.reqBuffer = new CompositeByteBuf(context.alloc(), false, Integer.MAX_VALUE)))
                    .addComponent(decodeBuffer.copy());
            }

            return decodeBuffer;
        }

        public boolean isDecodeData() {
            return this.decodeData;
        }

        @Nullable
        public String getRequest() {
            return this.req;
        }
    }

    private class SmtpServerRequestHandler extends AbstractToolServerRequestHandler<String> {
        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            Channel channel = context.channel();
            SmtpServerRequestDecoder reqDecoder = context.pipeline().get(SmtpServerRequestDecoder.class);
            String req = reqDecoder.getRequest();

            try {
                super.exceptionCaught(context, cause);
            } catch (Exception e) {
                LOGGER.error(String.format("Unable to process SMTP server request: %s", req), e);

                if ((cause = e.getCause()) instanceof ReadTimeoutException) {
                    SmtpServerImpl.this.writeResponse(channel, channel.attr(SERVER_SESSION_ATTR_KEY).get(), new SmtpReplyImpl(
                        SmtpReplyCode.SERVICE_UNAVAILABLE, String.format("%s read timeout.", (reqDecoder.isDecodeData() ? "Data" : "Command"))));
                } else if (cause instanceof TooLongFrameException) {
                    SmtpServerImpl.this.writeResponse(channel, channel.attr(SERVER_SESSION_ATTR_KEY).get(), new SmtpReplyImpl(
                        SmtpReplyCode.SERVICE_UNAVAILABLE, String.format("%s content was too long.", (reqDecoder.isDecodeData() ? "Data" : "Command"))));
                } else {
                    SmtpServerImpl.this.writeResponse(channel, channel.attr(SERVER_SESSION_ATTR_KEY).get(), true, new SmtpReplyImpl(
                        SmtpReplyCode.SERVICE_UNAVAILABLE, "Internal server error"));
                }
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext context, String req) throws Exception {
            Channel channel = context.channel();
            ChannelPipeline pipeline = channel.pipeline();
            boolean decodeData = pipeline.get(SmtpServerRequestDecoder.class).isDecodeData();
            SmtpServerSession serverSession = context.channel().attr(SERVER_SESSION_ATTR_KEY).get();
            LinkedList<SmtpCommand> cmds = serverSession.getCommands();

            pipeline.remove(ReadTimeoutHandler.class);

            if (decodeData) {
                ToolMimeMessageHelper mimeMsgHelper;

                try (ByteArrayInputStream dataInStream = new ByteArrayInputStream(req.getBytes(CharsetUtil.US_ASCII))) {
                    serverSession.setMimeMessageHelper((mimeMsgHelper =
                        new ToolMimeMessageHelper(new MimeMessage(SmtpServerImpl.this.appContext.getBean(SmtpServerImpl.this.mailSessionPlainBeanName,
                            Session.class), dataInStream), CharsetUtil.US_ASCII)));
                }

                pipeline.replace(REQ_DECODER_NAME, REQ_DECODER_NAME, new SmtpServerRequestDecoder(false));

                this.processData(mimeMsgHelper);

                SmtpServerImpl.this.writeResponse(channel, serverSession, new SmtpReplyImpl(SmtpReplyCode.MAIL_OK, SmtpReplyParameters.OK));

                return;
            }

            if (!cmds.isEmpty() && (cmds.getLast() instanceof AuthCommand)) {
                if (!serverSession.hasAuthenticationId()) {
                    serverSession.setAuthenticationId(new String(Base64.decodeBase64(req), CharsetUtil.US_ASCII));

                    SmtpServerImpl.this.writeResponse(channel, serverSession, new SmtpReplyImpl(SmtpReplyCode.AUTH_READY,
                        SmtpReplyParameters.AUTH_SECRET_PROMPT));

                    return;
                } else if (!serverSession.hasAuthenticationSecret()) {
                    String authSecret = new String(Base64.decodeBase64(req), CharsetUtil.US_ASCII);
                    serverSession.setAuthenticationSecret(authSecret);

                    InstanceMailAddressConfig authenticatedConfig =
                        SmtpServerImpl.this.userRepo.findAuthenticatedConfig(serverSession.getAuthenticationId(), authSecret);

                    if (authenticatedConfig != null) {
                        serverSession.setAuthenticatedConfig(authenticatedConfig);

                        SmtpServerImpl.this.writeResponse(channel, serverSession, new SmtpReplyImpl(SmtpReplyCode.AUTH_OK, "Authentication successful"));
                    } else {
                        serverSession.resetAuthentication();

                        SmtpServerImpl.this.writeResponse(channel, serverSession, new SmtpReplyImpl(SmtpReplyCode.AUTH_FAILED, "Authentication failed"));
                    }

                    return;
                }
            }

            String[] reqParts = StringUtils.split(req, StringUtils.SPACE, 2);
            SmtpCommandType cmdType = ToolEnumUtils.findById(SmtpCommandType.class, reqParts[0].toUpperCase());

            if (cmdType == null) {
                SmtpServerImpl.this.writeResponse(channel, serverSession, new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_COMMAND_UNRECOGNIZED,
                    "Command not recognized"));

                return;
            }

            try {
                SmtpCommand cmd = parseCommand(cmdType, ((reqParts.length == 2) ? StringUtils.stripEnd(reqParts[1], StringUtils.SPACE) : StringUtils.EMPTY));
                SmtpReply resp = cmd.process(channel);

                if (cmd instanceof DataCommand) {
                    pipeline.replace(REQ_DECODER_NAME, REQ_DECODER_NAME, new SmtpServerRequestDecoder(true));
                }

                if (!(cmd instanceof RsetCommand)) {
                    cmds.add(cmd);
                }

                SmtpServerImpl.this.writeResponse(channel, serverSession, resp);
            } catch (SmtpCommandException e) {
                SmtpServerImpl.this.writeResponse(channel, serverSession, e.getResponse());
            }
        }

        private void processData(ToolMimeMessageHelper mimeMsgHelper) throws Exception {
            MailAddress to = mimeMsgHelper.getTo(), from = mimeMsgHelper.getFrom();
            DiscoveryTestcase discoveryTestcase =
                ToolBeanFactoryUtils.getBeansOfType(SmtpServerImpl.this.appContext, DiscoveryTestcase.class).stream()
                    .filter(discoveryTestcaseSearch -> Objects.equals(discoveryTestcaseSearch.getMailAddress(), to)).findFirst().orElse(null);

            if (discoveryTestcase == null) {
                LOGGER.error(String.format("Unable to find associated Discovery testcase for mail (from=%s, to=%s).", from, to));

                return;
            }

            // noinspection ConstantConditions
            DiscoveryTestcaseMailMapping mailMapping =
                ToolBeanFactoryUtils.getBeanOfType(SmtpServerImpl.this.appContext, DiscoveryTestcaseMailMappingService.class).getBeans().stream()
                    .filter(discoveryTestcaseMailMapping -> Objects.equals(discoveryTestcaseMailMapping.getDirectAddress(), from)).findFirst().orElse(null);

            if (mailMapping != null) {
                // noinspection ConstantConditions
                this.sendResultMail(
                    to,
                    discoveryTestcase,
                    ToolBeanFactoryUtils.getBeanOfType(SmtpServerImpl.this.appContext, DiscoveryTestcaseProcessor.class).process(
                        ToolBeanFactoryUtils.createBeanOfType(SmtpServerImpl.this.appContext, DiscoveryTestcaseSubmission.class, discoveryTestcase,
                            mimeMsgHelper)), mailMapping);
            } else {
                LOGGER.error(String.format(
                    "Unable to find mail address for sending results associated with Discovery testcase (name=%s, mailAddr=%s) mail (from=%s).",
                    discoveryTestcase.getName(), to, from));
            }
        }

        private void sendResultMail(MailAddress to, DiscoveryTestcase discoveryTestcase, DiscoveryTestcaseResult discoveryTestcaseResult,
            DiscoveryTestcaseMailMapping mailMapping) throws Exception {
            DiscoveryTestcaseResultSenderService discoveryTestcaseResultSenderService =
                ToolBeanFactoryUtils.getBeanOfType(SmtpServerImpl.this.appContext, DiscoveryTestcaseResultSenderService.class);
            MailAddress resultsAddr = mailMapping.getResultsAddress();
            // noinspection ConstantConditions
            discoveryTestcaseResultSenderService.send(discoveryTestcaseResult, resultsAddr);

            LOGGER.info(String.format(
                "Sent Discovery testcase result mail (to=%s) for Discovery testcase (name=%s, mailAddr=%s) from sender service (class=%s).", resultsAddr,
                discoveryTestcase.getName(), to, ToolClassUtils.getName(DiscoveryTestcaseResultSenderService.class)));
        }
    }

    private class SmtpServerChannelInitializer extends AbstractMailServerChannelInitializer {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            super.initChannel(channel);

            SmtpServerSession serverSession = new SmtpServerSessionImpl();
            channel.attr(SERVER_SESSION_ATTR_KEY).set(serverSession);

            ChannelPipeline channelPipeline = channel.pipeline();
            channelPipeline.addLast(REQ_DECODER_NAME, new SmtpServerRequestDecoder(false));
            channelPipeline.addLast(STR_DECODER);
            channelPipeline.addLast(new SmtpServerRequestHandler());

            SmtpServerImpl.this.writeResponse(channel, serverSession, new SmtpReplyImpl(SmtpReplyCode.SERVICE_READY, (SmtpServerImpl.this.config.getGreeting()
                + StringUtils.SPACE + SmtpReplyParameters.ESMTP)));
        }
    }

    public final static AttributeKey<SmtpServerSession> SERVER_SESSION_ATTR_KEY = AttributeKey.valueOf("serverSession");

    private final static ByteBuf CMD_DELIM_BUFFER = Unpooled.wrappedBuffer(ToolStringUtils.CRLF_BYTES);
    private final static ByteBuf DATA_DELIM_BUFFER = Unpooled.wrappedBuffer(new byte[] { '\r', '\n', '.', '\r', '\n' });

    private final static String REQ_DECODER_NAME = "reqDecoder";

    private final static StringDecoder STR_DECODER = new StringDecoder(CharsetUtil.US_ASCII);

    private final static Logger LOGGER = LoggerFactory.getLogger(SmtpServerImpl.class);

    public SmtpServerImpl(SmtpServerConfig config, String mailSessionPlainBeanName, String mailSessionSslBeanName) {
        super(config, mailSessionPlainBeanName, mailSessionSslBeanName);
    }

    @Override
    protected ServerBootstrap initializeBootstrap(ServerBootstrap bootstrap) {
        return super.initializeBootstrap(bootstrap).childHandler(new SmtpServerChannelInitializer());
    }

    private static SmtpCommand parseCommand(SmtpCommandType cmdType, String str) throws SmtpCommandException {
        switch (cmdType) {
            case AUTH:
                return AuthCommand.parse(str);

            case DATA:
                return DataCommand.parse(str);

            case EHLO:
                return EhloCommand.parse(str);

            case HELO:
                return HeloCommand.parse(str);

            case MAIL:
                return MailCommand.parse(str);

            case QUIT:
                return QuitCommand.parse(str);

            case RCPT:
                return RcptCommand.parse(str);

            case RSET:
                return RsetCommand.parse(str);

            case STARTTLS:
                return StartTlsCommand.parse(str);

            default:
                return VrfyCommand.parse(str);
        }
    }

    private void writeResponse(Channel channel, SmtpServerSession serverSession, SmtpReply resp) throws Exception {
        this.writeResponse(channel, serverSession, false, resp);
    }

    private void writeResponse(Channel channel, SmtpServerSession serverSession, boolean close, SmtpReply resp) throws Exception {
        LinkedList<SmtpCommand> cmds = serverSession.getCommands();
        boolean quit = (!cmds.isEmpty() && (cmds.getLast() instanceof QuitCommand));

        if (quit) {
            close = true;
        } else {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new ReadTimeoutHandler((pipeline.get(SmtpServerRequestDecoder.class).isDecodeData()
                ? this.config.getDataReadTimeout() : this.config.getCommandReadTimeout()), TimeUnit.MILLISECONDS));
        }

        ChannelFuture future = channel.writeAndFlush(resp.toBuffer());

        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        future.sync();
    }
}
