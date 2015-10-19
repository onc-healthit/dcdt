package gov.hhs.onc.dcdt.service.mail.james.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.impl.DnsNameServiceImpl;
import gov.hhs.onc.dcdt.service.mail.james.ToolDnsService;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.james.dnsservice.api.TemporaryResolutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.TXTRecord;

public class ToolDnsServiceImpl extends DnsNameServiceImpl implements ToolDnsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolDnsServiceImpl.class);

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public Collection<String> findTXTRecords(String hostNameStr) {
        try {
            List<TXTRecord> txtRecords = this.lookupRecords(DnsRecordType.TXT, TXTRecord.class, hostNameStr, -1);

            return ((txtRecords != null) ? txtRecords.stream().flatMap(txtRecord -> ((List<String>) txtRecord.getStrings()).stream())
                .collect(Collectors.toList()) : Collections.emptyList());
        } catch (UnknownHostException e) {
            LOGGER.error(String.format("Unable parse DNS lookup (recordType=%s) name: %s", DnsRecordType.TXT, hostNameStr), e);

            return Collections.emptyList();
        }
    }

    @Override
    public Collection<String> findMXRecords(String hostNameStr) throws TemporaryResolutionException {
        try {
            List<MXRecord> mxRecords = this.lookupRecords(DnsRecordType.MX, MXRecord.class, hostNameStr, -1);

            return ((mxRecords != null) ? mxRecords.stream().map(mxRecord -> mxRecord.getTarget().toString()).collect(Collectors.toList()) : Collections
                .emptyList());
        } catch (UnknownHostException e) {
            LOGGER.error(String.format("Unable parse DNS lookup (recordType=%s) name: %s", DnsRecordType.MX, hostNameStr), e);

            return Collections.emptyList();
        }
    }

    @Nullable
    @Override
    public String getHostName(InetAddress addr) {
        return Optional.ofNullable(ToolListUtils.getFirst(this.lookupRecords(DnsRecordType.PTR, PTRRecord.class, ReverseMap.fromAddress(addr), 1)))
            .map(ptrRecord -> ptrRecord.getTarget().toString()).orElseGet(addr::getHostAddress);
    }

    @Override
    public InetAddress getLocalHost() throws UnknownHostException {
        return this.localhostAddr;
    }
}
