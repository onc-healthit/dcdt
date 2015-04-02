package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public interface DnsLookupResult<T extends Record> extends ToolLookupResultBean<T, DnsResultType> {
    public boolean hasAliases();

    @Nullable
    public List<Name> getAliases();

    public boolean hasAnswers();

    @Nullable
    public List<T> getAnswers();

    public Lookup getLookup();

    public boolean hasOrderedAnswers();

    @Nullable
    public List<T> getOrderedAnswers();

    public Name getQuestionName();

    public boolean hasRawAnswers();

    @Nullable
    public List<Record> getRawAnswers();

    public Class<T> getRecordClass();

    public boolean hasRecordPredicate();

    @Nullable
    public Predicate<T> getRecordPredicate();

    public DnsRecordType getRecordType();
}
