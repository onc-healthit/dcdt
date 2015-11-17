package gov.hhs.onc.dcdt.dns.lookup.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.lookup.DnsNameService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

public class DnsNameServiceImpl extends AbstractToolBean implements DnsNameService {
    @Resource(name = "ipAddrLocalhost")
    protected InetAddress localhostAddr;

    protected DnsLookupService[] lookupServices;
    protected String localhostHostAddr;
    protected String localhostHostName;

    @Override
    public InetAddress getByName(String host) throws UnknownHostException {
        return ToolArrayUtils.getFirst(this.getAllByName(host, 1));
    }

    @Override
    public InetAddress[] getAllByName(String host) throws UnknownHostException {
        return this.getAllByName(host, -1);
    }

    @Override
    public InetAddress[] getAllByName(String host, int limit) throws UnknownHostException {
        String relativeHost = StringUtils.stripEnd(host, ToolDnsNameUtils.DNS_NAME_DELIM);

        if (ToolInetAddressUtils.isAddress(relativeHost)) {
            return ArrayUtils
                .toArray((relativeHost.equals(this.localhostHostAddr) ? this.localhostAddr : ToolInetAddressUtils.getByAddress(null, relativeHost)));
        }

        if (relativeHost.equalsIgnoreCase(this.localhostHostName)) {
            return ArrayUtils.toArray(this.localhostAddr);
        }

        List<ARecord> aRecords = this.lookupRecords(DnsRecordType.A, ARecord.class, host, limit);

        if (CollectionUtils.isEmpty(aRecords)) {
            throw new UnknownHostException(host);
        }

        return aRecords.stream().map(ARecord::getAddress).toArray(InetAddress[]::new);
    }

    @Nullable
    @Override
    public <T extends Record> List<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, String hostNameStr, int limit) throws UnknownHostException {

        try {
            return this.lookupRecords(recordType, recordClass, Name.fromString(hostNameStr), limit);
        } catch (TextParseException e) {
            throw new UnknownHostException(String.format("Unable to parse DNS host name: %s", hostNameStr));
        }
    }

    @Nullable
    @Override
    public <T extends Record> List<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name hostName, int limit) {
        DnsLookupResult<T> result;

        for (DnsLookupService lookupService : this.lookupServices) {
            if ((result = lookupService.lookupRecords(recordType, recordClass, hostName)).isSuccess()) {
                List<T> orderedAnswers = result.getOrderedAnswers();

                // noinspection ConstantConditions
                return ((ToolNumberUtils.isPositive(limit) && (CollectionUtils.size(orderedAnswers) > limit))
                    ? orderedAnswers.subList(0, limit) : orderedAnswers);
            }
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.localhostHostAddr = this.localhostAddr.getHostAddress();
        this.localhostHostName = this.localhostAddr.getHostName();
    }

    @Override
    public DnsLookupService[] getLookupServices() {
        return this.lookupServices;
    }

    @Override
    public void setLookupServices(DnsLookupService ... lookupServices) {
        this.lookupServices = lookupServices;
    }
}
