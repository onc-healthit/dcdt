package gov.hhs.onc.dcdt.discovery.steps.dns.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.steps.dns.DnsSrvRecordLookupStep;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import org.apache.commons.lang3.ArrayUtils;
import org.xbill.DNS.Name;
import org.xbill.DNS.SRVRecord;

public class DnsSrvRecordLookupStepImpl extends AbstractDnsLookupStep<SRVRecord> implements DnsSrvRecordLookupStep {
    public DnsSrvRecordLookupStepImpl(DnsLookupService lookupService) {
        super(BindingType.DOMAIN, lookupService, DnsRecordType.SRV, SRVRecord.class);
    }
    
    @Override
    protected Name buildDirectAddressName(MailAddress directAddr) throws DnsException, ToolMailAddressException {
        return ToolDnsNameUtils.toAbsolute(ToolDnsNameUtils.fromLabels(DnsServiceType.LDAP.getNameLabel(), DnsServiceProtocol.TCP.getNameLabel(),
            super.buildDirectAddressName(directAddr)));
    }

    @Override
    protected Object[] getCloneArguments() {
        return ArrayUtils.toArray(this.lookupService);
    }
}
