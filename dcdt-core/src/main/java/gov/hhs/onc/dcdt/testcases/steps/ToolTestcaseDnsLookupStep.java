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
    public <T extends Record> List<T> getRecords();

    public <T extends Record> void setRecords(@Nullable List<T> records);
}
