package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsMessageOpcode;
import gov.hhs.onc.dcdt.dns.DnsMessageRcode;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils.DnsRecordConfigTransformer;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils.DnsRecordTargetTransformer;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractSocketRequestProcessor;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessingException;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequestProcessor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.PredicateUtils;
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

        // DNS query operations are permitted / "implemented".
        if (ToolDnsMessageUtils.getOpcode(reqMsg) != DnsMessageOpcode.QUERY) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.NOTIMP);

            return respMsg;
        } else
        // A DNS query without a question record does not make sense.
        if (questionRecord == null) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.FORMERR);

            return respMsg;
        }

        DnsRecordType questionRecordType = ToolDnsUtils.findByCode(DnsRecordType.class, questionRecord.getType());
        Name questionName;

        // @formatter:off
        /*
        Rejecting DNS question record (as "not implemented"):
        - Unknown type.
        - Mismatched class.
        - Not "processed" (i.e. not one of the types that can/will be served).
        - Name is not absolute.
        - Name is a wildcard.
        */
        // @formatter:on
        if ((questionRecordType == null) || (questionRecord.getDClass() != questionRecordType.getDclassType().getCode()) || !questionRecordType.isProcessed()
            || !(questionName = questionRecord.getName()).isAbsolute() || questionName.isWild()) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.NOTIMP);

            return respMsg;
        }

        List<InstanceDnsConfig> authoritativeConfigs = this.serverConfig.findAuthoritativeConfigs(questionRecord);
        int numAuthoritativeConfigs = authoritativeConfigs.size();

        // Refusing to resolve vs. external domain(s).
        if (numAuthoritativeConfigs == 0) {
            ToolDnsMessageUtils.setRcode(respMsg, DnsMessageRcode.REFUSED);

            return respMsg;
        }

        List<Record> answerRecords = new ArrayList<>(numAuthoritativeConfigs), configAnswerRecords;
        Set<Record> authorityRecords = new LinkedHashSet<>(2);
        SOARecord negAuthorityRecord = null, configNegAuthorityRecord = null;
        Name negAuthorityName = null, configNegAuthorityName = null;

        for (InstanceDnsConfig authoritativeConfig : authoritativeConfigs) {
            // noinspection ConstantConditions
            if (!CollectionUtils.isEmpty((configAnswerRecords = authoritativeConfig.findAnswers(questionRecordType, questionName)))) {
                // noinspection ConstantConditions
                answerRecords.addAll(configAnswerRecords);

                // If DNS SOA record answer resolved, add associated DNS NS record(s) as authorities.
                if (questionRecordType == DnsRecordType.SOA) {
                    ToolCollectionUtils.addAll(authorityRecords,
                        CollectionUtils.collect(authoritativeConfig.getNsRecordConfigs(), DnsRecordConfigTransformer.INSTANCE));

                    break;
                }
            } else
            // Determining "most authoritative" available DNS SOA record for use as an authority if no answer(s) are resolved.
            // noinspection ConstantConditions
            if ((questionRecordType != DnsRecordType.PTR) && answerRecords.isEmpty()
                && ((configNegAuthorityName = (configNegAuthorityRecord = authoritativeConfig.getSoaRecordConfig().toRecord()).getName()) != null)
                && (((negAuthorityRecord == null) && (negAuthorityName == null)) || configNegAuthorityName.subdomain(negAuthorityName))) {
                // noinspection ConstantConditions
                negAuthorityRecord = configNegAuthorityRecord;
                // noinspection ConstantConditions
                negAuthorityName = configNegAuthorityName;
            }
        }

        Set<Name> additionalNames =
            CollectionUtils.select(CollectionUtils.collect(
                IteratorUtils.asIterable(IteratorUtils.chainedIterator(answerRecords.iterator(), authorityRecords.iterator())),
                DnsRecordTargetTransformer.INSTANCE), PredicateUtils.notNullPredicate(),
                new LinkedHashSet<Name>((answerRecords.size() + authorityRecords.size())));
        Set<Record> additionalRecords = new LinkedHashSet<>(additionalNames.size());

        // Resolving IPv4 addresses (via DNS A record[s]) for all answer + authority DNS record(s) where a follow-up resolution can be avoided.
        for (Name additionalName : additionalNames) {
            for (InstanceDnsConfig additionalAuthoritativeConfig : this.serverConfig.findAuthoritativeConfigs(DnsRecordType.A, additionalName)) {
                ToolCollectionUtils.addAll(additionalRecords, additionalAuthoritativeConfig.findAnswers(DnsRecordType.A, additionalName));
            }
        }

        if ((questionRecordType != DnsRecordType.PTR) && answerRecords.isEmpty() && (negAuthorityRecord != null)) {
            // noinspection ConstantConditions
            authorityRecords.add(negAuthorityRecord);
        }

        ToolDnsMessageUtils.setAnswers(respMsg, answerRecords);
        ToolDnsMessageUtils.setAuthorities(respMsg, true, authorityRecords);
        ToolDnsMessageUtils.setAdditional(respMsg, additionalRecords);

        return respMsg;
    }
}
