package gov.hhs.onc.dcdt.service.mail.smtp.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailEncoding;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.impl.MailInfoImpl;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessage;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReply;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyParameters;
import gov.hhs.onc.dcdt.mail.smtp.SmtpTransportProtocol;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.AuthCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.DataCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.EhloCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.HeloCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.MailCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.NoopCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.QuitCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.RcptCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.impl.RsetCommand;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.service.mail.server.RemoteMailSenderService;
import gov.hhs.onc.dcdt.service.mail.server.impl.AbstractMailServer;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServer;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerConfig;
import gov.hhs.onc.dcdt.service.mail.smtp.SmtpServerSession;
import gov.hhs.onc.dcdt.service.mail.smtp.command.SmtpCommandProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingService;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
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
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@AutoStartup(false)
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 4)
public class SmtpServerImpl extends AbstractMailServer<SmtpTransportProtocol, SmtpServerConfig> implements SmtpServer {
    private class SmtpServerMailDeliveryTask implements Runnable {
        private MailInfo mailInfo;
        private MailAddress fromAddr;
        private MailAddress toAddr;
        private InstanceMailAddressConfig toConfig;
        private boolean toLocal;

        public SmtpServerMailDeliveryTask(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, @Nullable InstanceMailAddressConfig toConfig,
            boolean toLocal) {
            this.mailInfo = mailInfo;
            this.fromAddr = fromAddr;
            this.toAddr = toAddr;
            this.toConfig = toConfig;
            this.toLocal = toLocal;
        }

        @Override
        public void run() {
            if (this.toLocal) {
                this.processLocalDelivery();
            } else {
                this.processRemoteDelivery();
            }
        }

        private void processRemoteDelivery() {
            try {
                SmtpServerImpl.this.remoteMailSenderService.send(this.mailInfo, this.fromAddr, this.toAddr,
                    SmtpServerImpl.this.config.getHeloName().toString(true));

                LOGGER.info(String.format("Mail MIME message (id=%s, from=%s, to=%s) remote delivery (from=%s, to=%s) was successful:\n%s",
                    this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(), this.fromAddr, this.toAddr, this.mailInfo.getMessage()));
            } catch (Exception e) {
                LOGGER.error(String.format("Mail MIME message (id=%s, from=%s, to=%s) remote delivery (from=%s, to=%s) failed:\n%s",
                    this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(), this.fromAddr, this.toAddr, this.mailInfo.getMessage()), e);
            }
        }

        private void processLocalDelivery() {
            if (!this.toConfig.isProcessed()) {
                LOGGER.error(String.format("Locally delivered (from=%s, to=%s) mail MIME message (id=%s, from=%s, to=%s) to a non-processed address:\n%s",
                    this.fromAddr, this.toAddr, this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(), this.mailInfo.getMessage()));

                return;
            }

            // noinspection ConstantConditions
            DiscoveryTestcase discoveryTestcase =
                (SmtpServerImpl.this.gateway.hasDiscoveryTestcaseAddresses()
                    ? SmtpServerImpl.this.gateway.getDiscoveryTestcaseAddresses().get(this.toAddr) : null);

            if (discoveryTestcase == null) {
                LOGGER.error(String.format(
                    "Locally delivered (from=%s, to=%s) mail MIME message (id=%s, from=%s, to=%s) does not have an associated Discovery testcase:\n%s",
                    this.fromAddr, this.toAddr, this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(), this.mailInfo.getMessage()));

                return;
            }

            DiscoveryTestcaseMailMapping mailMapping =
                SmtpServerImpl.this.discoveryTestcaseMailMappingService.getBeans().stream()
                    .filter(discoveryTestcaseMailMapping -> Objects.equals(discoveryTestcaseMailMapping.getDirectAddress(), this.fromAddr)).findFirst()
                    .orElse(null);

            if (mailMapping == null) {
                LOGGER
                    .error(String
                        .format(
                            "Locally delivered (from=%s, to=%s) mail MIME message (id=%s, from=%s, to=%s) does not have an associated Discovery testcase result mail mapping:\n%s",
                            this.fromAddr, this.toAddr, this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(),
                            this.mailInfo.getMessage()));

                return;
            }

            MailAddress resultsAddr = mailMapping.getResultsAddress();

            try {
                SmtpServerImpl.this.discoveryTestcaseResultSenderService.send(SmtpServerImpl.this.discoveryTestcaseProc.process(ToolBeanFactoryUtils
                    .createBeanOfType(SmtpServerImpl.this.appContext, DiscoveryTestcaseSubmission.class, discoveryTestcase, this.mailInfo)), resultsAddr);

                LOGGER.info(String.format(
                    "Sent Discovery testcase results (resultsAddr=%s) for locally delivered (from=%s, to=%s) mail MIME message (id=%s, from=%s, to=%s):\n%s",
                    resultsAddr, this.fromAddr, this.toAddr, this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(),
                    this.mailInfo.getMessage()));
            } catch (Exception e) {
                LOGGER
                    .error(
                        String
                            .format(
                                "Unable to send Discovery testcase results (resultsAddr=%s) for locally delivered (from=%s, to=%s) mail MIME message (id=%s, from=%s, to=%s):\n%s",
                                resultsAddr, this.fromAddr, this.toAddr, this.mailInfo.getMessageId(), this.mailInfo.getFrom(), this.mailInfo.getTo(),
                                this.mailInfo.getMessage()), e);
            }
        }
    }

    private class SmtpServerRequestDecoder extends DelimiterBasedFrameDecoder {
        private boolean decodeData;
        private CompositeByteBuf reqBuffer;

        public SmtpServerRequestDecoder(boolean decodeData) {
            super((decodeData ? SmtpServerImpl.this.config.getMaxDataFrameLength() : SmtpServerImpl.this.config.getMaxCommandFrameLength()), true, false,
                (decodeData ? DATA_DELIM_BUFFER : CMD_DELIM_BUFFER));

            this.setSingleDecode((this.decodeData = decodeData));
        }

        @Nullable
        @Override
        protected Object decode(ChannelHandlerContext context, ByteBuf decodeBuffer) throws Exception {
            if ((decodeBuffer = ((ByteBuf) super.decode(context, decodeBuffer))) != null) {
                ((this.reqBuffer != null) ? this.reqBuffer : (this.reqBuffer = new CompositeByteBuf(context.alloc(), false, Integer.MAX_VALUE)))
                    .addComponent(decodeBuffer.copy());
            }

            return decodeBuffer;
        }

        public boolean isDecodeData() {
            return this.decodeData;
        }

        public boolean hasRequestBuffer() {
            return (this.reqBuffer != null);
        }

        @Nullable
        public CompositeByteBuf getRequestBuffer() {
            return this.reqBuffer;
        }
    }

    private class SmtpServerRequestHandler extends AbstractToolServerRequestHandler<ByteBuf> {
        @Override
        public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
            Channel channel = context.channel();
            ChannelPipeline pipeline = channel.pipeline();
            SmtpServerRequestDecoder reqDecoder = context.pipeline().get(SmtpServerRequestDecoder.class);
            SmtpServerSession session = context.channel().attr(SESSION_ATTR_KEY).get();

            pipeline.remove(ReadTimeoutHandler.class);

            // noinspection ConstantConditions
            String req = (reqDecoder.hasRequestBuffer() ? reqDecoder.getRequestBuffer().toString(CharsetUtil.US_ASCII) : null);

            try {
                super.exceptionCaught(context, cause);
            } catch (Exception e) {
                LOGGER.error(String.format("Unable to process SMTP server session (from=%s, to=%s) request: %s", session.getFrom(), session.getTo(), req), e);

                boolean decodeData = reqDecoder.isDecodeData();

                if ((cause = e.getCause()) instanceof ReadTimeoutException) {
                    SmtpServerImpl.this.writeResponse(channel, channel.attr(SESSION_ATTR_KEY).get(), new SmtpReplyImpl(SmtpReplyCode.SERVICE_UNAVAILABLE,
                        String.format("%s read timeout.", (decodeData ? "Data" : "Command"))));
                } else if (cause instanceof TooLongFrameException) {
                    SmtpServerImpl.this.writeResponse(
                        channel,
                        channel.attr(SESSION_ATTR_KEY).get(),
                        new SmtpReplyImpl((decodeData ? SmtpReplyCode.SYSTEM_STORAGE_ERROR : SmtpReplyCode.SYNTAX_ERROR_COMMAND), String.format(
                            "%s content was too long.", (decodeData ? "Data" : "Command"))));
                } else {
                    SmtpServerImpl.this.writeResponse(channel, channel.attr(SESSION_ATTR_KEY).get(), true, new SmtpReplyImpl(SmtpReplyCode.SERVICE_UNAVAILABLE,
                        "Internal server error"));
                }
            }
        }

        @Override
        @SuppressWarnings({ CompilerWarnings.UNCHECKED })
        protected void channelRead0(ChannelHandlerContext context, ByteBuf reqBuffer) throws Exception {
            Channel channel = context.channel();
            ChannelPipeline pipeline = channel.pipeline();
            boolean decodeData = pipeline.get(SmtpServerRequestDecoder.class).isDecodeData();
            SmtpServerSession session = context.channel().attr(SESSION_ATTR_KEY).get();
            LinkedList<SmtpCommand> cmds = session.getCommands();

            pipeline.remove(ReadTimeoutHandler.class);

            byte[] reqContent = new byte[reqBuffer.readableBytes()];
            reqBuffer.readBytes(reqContent);

            String req = new String(reqContent, CharsetUtil.US_ASCII);

            if (decodeData) {
                pipeline.replace(REQ_DECODER_NAME, REQ_DECODER_NAME, new SmtpServerRequestDecoder(false));

                boolean toLocal = session.hasToConfig();

                if (toLocal || (session.hasAuthenticatedAddressConfig() && session.hasFromConfig())) {
                    MailInfo mailInfo = null;

                    try {
                        mailInfo = new MailInfoImpl(new ToolMimeMessage(SmtpServerImpl.this.mailSession, reqContent), MailEncoding.UTF_8);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to process SMTP server session (from=%s, to=%s) mail MIME message:\n%s", session.getFrom(),
                            session.getTo(), req), e);

                        SmtpServerImpl.this.writeResponse(
                            channel,
                            session,
                            new SmtpReplyImpl(SmtpReplyCode.MAILBOX_PERM_UNAVAILABLE, String.format("Malformed mail MIME message: from=%s, to=%s",
                                session.getFrom(), session.getTo())));
                    }

                    if (mailInfo != null) {
                        SmtpServerImpl.this.reqTaskExec.submit(new SmtpServerMailDeliveryTask(mailInfo, session.getFrom(), session.getTo(), session
                            .getToConfig(), toLocal));

                        SmtpServerImpl.this.writeResponse(channel, session, new SmtpReplyImpl(SmtpReplyCode.MAIL_OK, SmtpReplyParameters.OK));
                    }
                } else {
                    SmtpServerImpl.this.writeResponse(
                        channel,
                        session,
                        new SmtpReplyImpl(SmtpReplyCode.MAILBOX_PERM_UNAVAILABLE, String.format("Unable to deliver mail MIME message: from=%s, to=%s",
                            session.getFrom(), session.getTo())));
                }

                return;
            }

            if (!cmds.isEmpty() && (cmds.getLast() instanceof AuthCommand)) {
                if (!session.hasAuthenticationId()) {
                    session.setAuthenticationId(new String(Base64.decodeBase64(req), CharsetUtil.UTF_8));

                    SmtpServerImpl.this.writeResponse(channel, session, new SmtpReplyImpl(SmtpReplyCode.AUTH_READY, SmtpReplyParameters.AUTH_SECRET_PROMPT));

                    return;
                } else if (!session.hasAuthenticationSecret()) {
                    String authSecret = new String(Base64.decodeBase64(req), CharsetUtil.UTF_8);
                    session.setAuthenticationSecret(authSecret);

                    InstanceMailAddressConfig authenticatedConfig = SmtpServerImpl.this.gateway.authenticate(session.getAuthenticationId(), authSecret);

                    if (authenticatedConfig != null) {
                        session.setAuthenticatedAddressConfig(authenticatedConfig);

                        SmtpServerImpl.this.writeResponse(channel, session, new SmtpReplyImpl(SmtpReplyCode.AUTH_OK, "Authentication successful"));
                    } else {
                        session.resetAuthentication();

                        SmtpServerImpl.this.writeResponse(channel, session, new SmtpReplyImpl(SmtpReplyCode.AUTH_FAILED, "Authentication failed"));
                    }

                    return;
                }
            }

            String[] reqParts = StringUtils.split(req, StringUtils.SPACE, 2);
            SmtpCommandType cmdType = ToolEnumUtils.findById(SmtpCommandType.class, reqParts[0].toUpperCase());

            if (cmdType == null) {
                SmtpServerImpl.this.writeResponse(channel, session, new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_COMMAND, "Command not recognized"));

                return;
            }

            try {
                SmtpCommand cmd = parseCommand(cmdType, ((reqParts.length == 2) ? StringUtils.stripEnd(reqParts[1], StringUtils.SPACE) : StringUtils.EMPTY));
                SmtpReply resp =
                    ((SmtpCommandProcessor<SmtpCommand>) SmtpServerImpl.this.cmdProcs.get(cmdType)).process(channel, SmtpServerImpl.this.gateway,
                        SmtpServerImpl.this.config, session, cmd);

                if (cmd instanceof DataCommand) {
                    pipeline.replace(REQ_DECODER_NAME, REQ_DECODER_NAME, new SmtpServerRequestDecoder(true));
                }

                if (!(cmd instanceof RsetCommand)) {
                    cmds.add(cmd);
                }

                SmtpServerImpl.this.writeResponse(channel, session, resp);
            } catch (SmtpCommandException e) {
                SmtpServerImpl.this.writeResponse(channel, session, e.getResponse());
            }
        }
    }

    private class SmtpServerChannelInitializer extends AbstractMailServerChannelInitializer {
        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            super.initChannel(channel);

            SmtpServerSession session = new SmtpServerSessionImpl();
            channel.attr(SESSION_ATTR_KEY).set(session);

            ChannelPipeline channelPipeline = channel.pipeline();
            channelPipeline.addLast(REQ_DECODER_NAME, new SmtpServerRequestDecoder(false));
            channelPipeline.addLast(new SmtpServerRequestHandler());

            SmtpServerImpl.this.writeResponse(channel, session, new SmtpReplyImpl(SmtpReplyCode.SERVICE_READY, (SmtpServerImpl.this.config.getGreeting()
                + StringUtils.SPACE + SmtpReplyParameters.ESMTP)));
        }
    }

    public final static AttributeKey<SmtpServerSession> SESSION_ATTR_KEY = AttributeKey.valueOf("session");

    private final static ByteBuf CMD_DELIM_BUFFER = Unpooled.wrappedBuffer(ToolStringUtils.CRLF_BYTES);
    private final static ByteBuf DATA_DELIM_BUFFER = Unpooled.wrappedBuffer(new byte[] { '\r', '\n', '.', '\r', '\n' });

    private final static String REQ_DECODER_NAME = "reqDecoder";

    private final static Logger LOGGER = LoggerFactory.getLogger(SmtpServerImpl.class);

    @Autowired
    private DiscoveryTestcaseMailMappingService discoveryTestcaseMailMappingService;

    @Autowired
    private DiscoveryTestcaseProcessor discoveryTestcaseProc;

    @Resource(name = "discoveryTestcaseResultSenderServiceImpl")
    private DiscoveryTestcaseResultSenderService discoveryTestcaseResultSenderService;

    @Resource(name = "remoteMailSenderServiceImpl")
    private RemoteMailSenderService remoteMailSenderService;

    private Map<SmtpCommandType, SmtpCommandProcessor<?>> cmdProcs;

    public SmtpServerImpl(SmtpServerConfig config) {
        super(config);
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

            case NOOP:
                return NoopCommand.parse(str);

            case QUIT:
                return QuitCommand.parse(str);

            case RCPT:
                return RcptCommand.parse(str);

            case RSET:
                return RsetCommand.parse(str);

            default:
                throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.COMMAND_UNIMPLEMENTED, String.format("%s command unsupported", cmdType.getId())));
        }
    }

    private ChannelFuture writeResponse(Channel channel, SmtpServerSession session, SmtpReply resp) throws Exception {
        return this.writeResponse(channel, session, false, resp);
    }

    private ChannelFuture writeResponse(Channel channel, SmtpServerSession session, boolean close, SmtpReply resp) throws Exception {
        LinkedList<SmtpCommand> cmds = session.getCommands();
        boolean quit = (!cmds.isEmpty() && (cmds.getLast() instanceof QuitCommand));

        if (quit) {
            close = true;
        }

        if (!close) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new ReadTimeoutHandler((pipeline.get(SmtpServerRequestDecoder.class).isDecodeData()
                ? this.config.getDataReadTimeout() : this.config.getCommandReadTimeout()), TimeUnit.MILLISECONDS));
        }

        ChannelFuture future = channel.writeAndFlush(resp.toBuffer());

        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        return future;
    }

    @Override
    public Map<SmtpCommandType, SmtpCommandProcessor<?>> getCommandProcessors() {
        return this.cmdProcs;
    }

    @Autowired
    private void setCommandProcessors(SmtpCommandProcessor<?> ... cmdProcs) {
        this.cmdProcs = Stream.of(cmdProcs).collect(ToolStreamUtils.toMap(SmtpCommandProcessor::getType, Function.identity(), LinkedHashMap::new));
    }
}
