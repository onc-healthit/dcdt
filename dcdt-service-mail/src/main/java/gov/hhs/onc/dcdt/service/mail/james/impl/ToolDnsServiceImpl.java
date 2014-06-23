package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import gov.hhs.onc.dcdt.service.mail.james.ToolDnsService;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.james.dnsservice.api.TemporaryResolutionException;
import org.apache.james.dnsservice.dnsjava.DNSJavaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Address;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

public class ToolDnsServiceImpl extends DNSJavaService implements ToolDnsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolDnsServiceImpl.class);

    @Resource(name = "ipAddrLocalhost")
    private InetAddress addrLocalhost;

    private Set<String> addrLocalhostNames;
    private DnsServiceConfigBean configBean;

    @Nullable
    @Override
    public InetAddress getByName(String name) throws UnknownHostException {
        return ToolArrayUtils.getFirst(this.getByName(name, 1));
    }

    @Nullable
    @Override
    public InetAddress[] getAllByName(String name) throws UnknownHostException {
        return this.getByName(name, -1);
    }

    @Nullable
    @Override
    public String getHostName(InetAddress addr) {
        return addr.getHostAddress();
    }

    @Override
    public Collection<String> findTXTRecords(String hostName) {
        return new ArrayList<>(0);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.addrLocalhostNames =
            ToolCollectionUtils.addAll(new TreeSet<>(String.CASE_INSENSITIVE_ORDER),
                ToolArrayUtils.asList(this.addrLocalhost.getHostName(), this.addrLocalhost.getCanonicalHostName(), this.addrLocalhost.getHostAddress()));
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void configure(HierarchicalConfiguration config) throws ConfigurationException {
    }

    @Nullable
    @Override
    protected Record[] lookup(String nameStr, int recordTypeId, String recordTypeDesc) throws TemporaryResolutionException {
        DnsRecordType recordType = ToolDnsUtils.findByCode(DnsRecordType.class, recordTypeId);

        if (recordType == null) {
            LOGGER.error(String.format("Unable to perform DNS lookup (name=%s) for unknown record type (desc=%s): %d", nameStr, recordTypeDesc, recordTypeId));

            return null;
        }

        Name name;

        try {
            name = Name.fromString(nameStr);
        } catch (TextParseException e) {
            LOGGER.error(String.format("Unable to parse DNS lookup (recordType=%s) name: %s", recordType.getId(), nameStr), e);

            return null;
        }

        DnsLookupResult<? extends Record> lookupResult;

        try {
            lookupResult = this.configBean.getLocalLookupService().lookupRecords(recordType, recordType.getRecordClass(), name);

            if (!lookupResult.isSuccess()) {
                lookupResult = this.configBean.getExternalLookupService().lookupRecords(recordType, recordType.getRecordClass(), name);
            }
        } catch (DnsException e) {
            LOGGER.error(String.format("Unable to perform DNS lookup (recordType=%s, name=%s) name.", recordType.getId(), nameStr), e);

            return null;
        }

        return ToolCollectionUtils.toArray(lookupResult.getAnswers(), Record.class);
    }

    @Nullable
    private InetAddress[] getByName(String name, int limit) throws UnknownHostException {
        InetAddress[] addrs;

        try {
            addrs = ArrayUtils.toArray((this.addrLocalhostNames.contains(name) ? this.addrLocalhost : Address.getByAddress(name, Address.IPv4)));
            addrs = (ToolNumberUtils.isPositive(limit) ? ArrayUtils.subarray(addrs, 0, limit) : addrs);
        } catch (UnknownHostException e) {
            Record[] addrRecords = this.lookupNoException(name, DnsRecordType.A.getCode(), DnsRecordType.A.getId());

            if (ArrayUtils.isEmpty((addrRecords = (ToolNumberUtils.isPositive(limit) ? ArrayUtils.subarray(addrRecords, 0, limit) : addrRecords)))) {
                throw e;
            }

            addrs = new InetAddress[addrRecords.length];

            for (int a = 0; a < addrRecords.length; a++) {
                addrs[a] = ToolInetAddressUtils.getByAddress(name, ((ARecord) addrRecords[a]).getAddress().getHostAddress());
            }
        }

        return addrs;
    }

    @Override
    public DnsServiceConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(DnsServiceConfigBean configBean) {
        this.configBean = configBean;
    }

    @Override
    public InetAddress getLocalHost() throws UnknownHostException {
        return this.addrLocalhost;
    }
}
