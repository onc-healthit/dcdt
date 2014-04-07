package gov.hhs.onc.dcdt.testcases.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Record;

public interface ToolTestcaseDnsLookupStep extends ToolTestcaseStep {
    @JsonProperty("dnsRecordType")
    public DnsRecordType getDnsRecordType();

    public void setDnsRecordType(DnsRecordType dnsRecordType);

    public boolean hasRecords();

    @Nullable
    public List<? extends Record> getRecords();

    public void setRecords(@Nullable List<? extends Record> records);
}
