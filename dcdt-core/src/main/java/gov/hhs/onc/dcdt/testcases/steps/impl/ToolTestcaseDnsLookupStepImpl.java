package gov.hhs.onc.dcdt.testcases.steps.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseDnsLookupStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.CERTRecord;
import java.util.List;
import java.util.ArrayList;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;

public class ToolTestcaseDnsLookupStepImpl extends AbstractToolTestcaseStep implements ToolTestcaseDnsLookupStep {
    @Autowired
    private DnsLookupService dnsLookupService;

    private DnsRecordType dnsRecordType;
    private List<? extends Record> records;

    @Override
    public DnsRecordType getDnsRecordType() {
        return this.dnsRecordType;
    }

    @Override
    public void setDnsRecordType(DnsRecordType dnsRecordType) {
        this.dnsRecordType = dnsRecordType;
    }

    @Override
    public boolean hasRecords() {
        return this.records != null && !this.records.isEmpty();
    }

    @Nullable
    @Override
    @SuppressWarnings({ "unchecked" })
    public List<? extends Record> getRecords() {
        return this.records;
    }

    @Override
    public void setRecords(@Nullable List<? extends Record> records) {
        this.records = records;
    }

    @Override
    public boolean execute(MailAddress directAddr, ToolTestcaseStep prevStep) throws ToolTestcaseResultException {
        Name directAddrName;
        try {
            directAddrName = directAddr.toAddressName(this.bindingType);
        } catch (ToolMailAddressException e) {
            throw new ToolTestcaseResultException(String.format("Unable to convert mail address %s for binding type=%s", directAddr.toAddress(),
                this.bindingType), e);
        }

        switch (this.dnsRecordType) {
            case CERT:
                this.records = getCertRecords(directAddrName);
                break;
            case SRV:
                this.records = getSrvRecords(directAddrName);
                break;
            default:
                break;
        }

        return hasRecords();
    }

    private List<SRVRecord> getSrvRecords(Name directAddrName) {
        List<SRVRecord> records = new ArrayList<>();
        DnsLookupResult<SRVRecord> dnsLookupResult;
        try {
            dnsLookupResult = this.dnsLookupService.lookupSrvRecords(DnsServiceType.LDAP, DnsServiceProtocol.TCP, directAddrName);
            records = dnsLookupResult.getResolvedAnswers();
            this.setMessage(dnsLookupResult.getErrorString());
        } catch (DnsException e) {
            this.setMessage(e.getMessage());
        }
        return records;
    }

    private List<CERTRecord> getCertRecords(Name directAddrName) {
        List<CERTRecord> records = new ArrayList<>();
        DnsLookupResult<CERTRecord> dnsLookupResult;
        try {
            dnsLookupResult = this.dnsLookupService.lookupCertRecords(directAddrName);
            records = dnsLookupResult.getResolvedAnswers();
            this.setMessage(dnsLookupResult.getErrorString());
        } catch (DnsException e) {
            this.setMessage(e.getMessage());
        }
        return records;
    }
}
