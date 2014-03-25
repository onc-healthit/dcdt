package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public interface DnsLookupResult<T extends Record> {
    public boolean hasAnswers();

    @Nullable
    public List<T> getAnswers();

    public Name getQuestionName();

    public Class<T> getRecordClass();

    public DnsRecordType getRecordType();

    public boolean hasErrorString();
    
    @Nullable
    public String getErrorString();
    
    public DnsResultType getType();
}
