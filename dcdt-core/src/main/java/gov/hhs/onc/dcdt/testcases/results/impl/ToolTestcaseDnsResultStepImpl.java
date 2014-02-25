package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.utils.SrvRecordUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseDnsResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import org.xbill.DNS.CERTRecord;
import java.util.List;
import java.util.ArrayList;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;

public class ToolTestcaseDnsResultStepImpl extends AbstractToolTestcaseResultStep implements ToolTestcaseDnsResultStep {
    private DnsLookupService dnsLookupService;
    private DnsRecordType dnsRecordType;

    @Override
    public DnsRecordType getDnsRecordType() {
        return this.dnsRecordType;
    }

    @Override
    public void setDnsRecordType(DnsRecordType dnsRecordType) {
        this.dnsRecordType = dnsRecordType;
    }

    @Override
    public DnsLookupService getDnsLookupService() {
        return this.dnsLookupService;
    }

    @Override
    public void setDnsLookupService(DnsLookupService dnsLookupService) {
        this.dnsLookupService = dnsLookupService;
    }

    @Override
    public boolean execute(ToolTestcaseResultHolder resultHolder, MailAddress directAddr) throws ToolTestcaseResultException {
        Name directAddrName;
        try {
            directAddrName = directAddr.toAddressName(this.bindingType);
        } catch (ToolMailAddressException e) {
            throw new ToolTestcaseResultException(String.format("Unable to convert mail address %s for binding type=%s", directAddr.toAddress(),
                this.bindingType.getBinding()), e);
        }
        List<? extends Record> records = new ArrayList<>();

        switch (this.dnsRecordType) {
            case CERT:
                records = getCertRecords(resultHolder, directAddrName);
                break;
            case SRV:
                records = getSrvRecords(resultHolder, directAddrName);
                break;
            default:
                break;
        }
        return records.size() > 0;
    }

    private List<SRVRecord> getSrvRecords(ToolTestcaseResultHolder resultHolder, Name directAddrName) {
        List<SRVRecord> records = new ArrayList<>();
        try {
            records = this.dnsLookupService.getSrvRecords(DnsServiceType.LDAP, DnsServiceProtocol.TCP, directAddrName);
            resultHolder.setSortedSrvRecords(SrvRecordUtils.sortSrvRecords(records));
        } catch (DnsException e) {
            this.setMessage(e.getMessage());
        }
        return records;
    }

    private List<CERTRecord> getCertRecords(ToolTestcaseResultHolder resultHolder, Name directAddrName) {
        List<CERTRecord> records = new ArrayList<>();
        try {
            records = this.dnsLookupService.getCertRecords(directAddrName);
            resultHolder.setCertRecords(records);
        } catch (DnsException e) {
            this.setMessage(e.getMessage());
        }
        return records;
    }
}
