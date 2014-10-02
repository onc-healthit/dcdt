package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsMessageOpcode;
import gov.hhs.onc.dcdt.dns.DnsMessageRcode;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractSocketRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessingException;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;

@Component("dnsServerReqProcImpl")
@Lazy
@Scope("prototype")
public class DnsServerRequestProcessorImpl extends AbstractSocketRequestProcessor<DnsServerRequest> implements DnsServerRequestProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(DnsServerRequestProcessorImpl.class);

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ConversionService convService;

    private DnsServerConfig serverConfig;

    public DnsServerRequestProcessorImpl(DnsServerConfig serverConfig, DnsServerRequest req) {
        super(req);

        this.serverConfig = serverConfig;
    }

    @Override
    protected byte[] processError(byte[] reqData, Exception exception) {
        LOGGER.error(exception.getMessage(), exception.getCause());

        return ToolDnsMessageUtils.createErrorResponse(((DnsServerRequestProcessingException) exception).getRequestMessage(), DnsMessageRcode.SERVFAIL)
            .toWire();
    }

    @Override
    protected byte[] processRequestInternal(byte[] reqData) throws Exception {
        InetProtocol protocol = this.req.getProtocol();
        Message reqMsg = null, respMsg = null;
        byte[] respData;

        try {
            respData = ToolDnsMessageUtils.toWire(protocol, (respMsg = this.resolveQuery((reqMsg = ToolDnsMessageUtils.fromWire(protocol, reqData)))));

            LOGGER.trace(String.format("Resolved (class=%s) DNS server request (protocol=%s, remoteSocketAddr={%s}):\n%s\n%s", ToolClassUtils.getName(this),
                protocol.name(), this.req.getRemoteAddress(), this.convService.convert(reqMsg, String.class), this.convService.convert(respMsg, String.class)));

            return respData;
        } catch (Exception e) {
            throw new DnsServerRequestProcessingException(reqMsg, respMsg, String.format(
                "Unable to resolve (class=%s) DNS server request (protocol=%s, remoteSocketAddr={%s}):\n%s\n%s", ToolClassUtils.getName(this), protocol.name(),
                this.req.getRemoteAddress(), reqMsg, respMsg), e);
        }
    }

    private Message resolveQuery(Message reqMsg) throws DnsException {
        Message respMsg = ToolDnsMessageUtils.createResponse(reqMsg);
        Record questionRecord = reqMsg.getQuestion();

        if (ToolDnsMessageUtils.getOpcode(reqMsg) != DnsMessageOpcode.QUERY) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.REFUSED);

            return respMsg;
        } else if (questionRecord == null) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.FORMERR);

            return respMsg;
        }

        DnsRecordType questionRecordType = ToolDnsUtils.findByCode(DnsRecordType.class, questionRecord.getType());
        Name questionName;

        if ((questionRecordType == null) || !questionRecordType.isProcessed()) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.NXRRSET);

            return respMsg;
        } else if (!(questionName = questionRecord.getName()).isAbsolute() || questionName.isWild()) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.REFUSED);

            return respMsg;
        }

        List<InstanceDnsConfig> authoritativeDnsConfigs = this.serverConfig.findAuthoritativeDnsConfigs(questionRecord);
        int numAuthoritativeDnsConfigs = authoritativeDnsConfigs.size();

        if (numAuthoritativeDnsConfigs == 0) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.REFUSED);

            return respMsg;
        }

        Collection<Record> answerRecords = new ArrayList<>(numAuthoritativeDnsConfigs), configAnswerRecords;
        List<SOARecord> authorityRecords = new ArrayList<>(numAuthoritativeDnsConfigs);

        for (InstanceDnsConfig authoritativeDnsConfig : authoritativeDnsConfigs) {
            if (!(configAnswerRecords = authoritativeDnsConfig.findAnswers(questionRecord)).isEmpty()) {
                answerRecords.addAll(configAnswerRecords);
                // noinspection ConstantConditions
                authorityRecords.add(authoritativeDnsConfig.getSoaRecordConfig().toRecord());
            }
        }

        ToolDnsMessageUtils.setAnswers(respMsg, answerRecords);
        ToolDnsMessageUtils.setAuthorities(respMsg, true, authorityRecords);

        return respMsg;
    }
}
