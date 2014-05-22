package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsMessageRcode;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractSocketRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessingException;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

@Component("dnsServerReqProcImpl")
@Lazy
@Scope("prototype")
public class DnsServerRequestProcessorImpl extends AbstractSocketRequestProcessor<DnsServerRequest> implements DnsServerRequestProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(DnsServerRequestProcessorImpl.class);

    private DnsServerConfig serverConfig;

    public DnsServerRequestProcessorImpl(DnsServerConfig serverConfig, DnsServerRequest req) {
        super(req);

        this.serverConfig = serverConfig;
    }

    @Override
    protected byte[] processError(byte[] reqData, Exception exception) {
        return ToolDnsMessageUtils.createErrorResponse(((DnsServerRequestProcessingException) exception).getRequestMessage(), DnsMessageRcode.SERVFAIL).toWire();
    }

    @Override
    protected byte[] processRequestInternal(byte[] reqData) throws Exception {
        InetProtocol protocol = this.req.getProtocol();
        Message reqMsg = null, respMsg = null;
        byte[] respData = null;

        try {
            respData = ToolDnsMessageUtils.toWire(protocol, (respMsg = this.resolveQuery((reqMsg = ToolDnsMessageUtils.fromWire(protocol, reqData)))));

            LOGGER.trace(String.format("Resolved (class=%s) DNS server query (protocol=%s, reqDataSize=%d, reqMsg={%s}): respDataSize=%d, respMsg={%s}",
                ToolClassUtils.getName(this), protocol.name(), reqData.length, reqMsg, ArrayUtils.getLength(respData), respMsg));

            return respData;
        } catch (Exception e) {
            throw new DnsServerRequestProcessingException(reqMsg, respMsg, String.format(
                "Unable to resolve (class=%s) DNS server query (protocol=%s, reqDataSize=%d, reqMsg={%s}): respDataSize=%d, respMsg={%s}",
                ToolClassUtils.getName(this), protocol.name(), reqData.length, reqMsg, ArrayUtils.getLength(respData), respMsg), e);
        }
    }

    private Message resolveQuery(Message reqMsg) throws DnsException {
        Message respMsg = ToolDnsMessageUtils.createResponse(reqMsg);
        Record questionRecord = reqMsg.getQuestion();

        if (questionRecord == null) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.FORMERR);

            return respMsg;
        }

        DnsRecordType questionRecordType = ToolDnsRecordUtils.findByType(questionRecord.getType());
        Name questionName;

        if (questionRecordType == null) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.NXRRSET);

            return respMsg;
        } else if (!(questionName = questionRecord.getName()).isAbsolute() || questionName.isWild()) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.REFUSED);

            return respMsg;
        }

        InstanceDnsConfig authoritativeDnsConfig = this.serverConfig.findAuthoritativeDnsConfig(questionRecord);

        if (authoritativeDnsConfig != null) {
            ToolDnsMessageUtils.setAnswers(respMsg, authoritativeDnsConfig.findAnswers(questionRecord));
            // noinspection ConstantConditions
            ToolDnsMessageUtils.setAuthorities(respMsg, true, authoritativeDnsConfig.getSoaRecordConfig().toRecord());
        } else {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.REFUSED);
        }

        return respMsg;
    }
}
