package gov.hhs.onc.dcdt.dns.lookup.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLookupResultBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordOrderUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Predicate;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public class DnsLookupResultImpl<T extends Record> extends AbstractToolLookupResultBean<T, DnsResultType> implements DnsLookupResult<T> {
    private Lookup lookup;
    private Name questionName;
    private Class<T> recordClass;
    private Predicate<T> recordPredicate;
    private DnsRecordType recordType;

    public DnsLookupResultImpl(DnsRecordType recordType, Class<T> recordClass, Name questionName, Lookup lookup, @Nullable Predicate<T> recordPredicate) {
        this.recordType = recordType;
        this.recordClass = recordClass;
        this.questionName = questionName;
        this.lookup = lookup;
        this.recordPredicate = recordPredicate;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Iterator<T> iterator() {
        return ((Iterator<T>) IteratorUtils.getIterator(this.getAnswers()));
    }

    @Override
    public boolean hasAliases() {
        return !CollectionUtils.isEmpty(this.getAliases());
    }

    @Nullable
    @Override
    public List<Name> getAliases() {
        return (this.isSuccess() ? ToolArrayUtils.asList(this.lookup.getAliases()) : null);
    }

    @Override
    public boolean hasAnswers() {
        return !CollectionUtils.isEmpty(this.getAnswers());
    }

    @Nullable
    @Override
    public List<T> getAnswers() {
        List<Record> rawAnswers;
        List<T> answers =
            (this.isSuccess() ? ToolCollectionUtils.collectAssignable(this.recordClass,
                new ArrayList<T>(CollectionUtils.size((rawAnswers = this.getRawAnswers()))), rawAnswers) : null);

        CollectionUtils.filter(answers, this.recordPredicate);

        return answers;
    }

    @Override
    public Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public boolean hasOrderedAnswers() {
        return !CollectionUtils.isEmpty(this.getOrderedAnswers());
    }

    @Nullable
    @Override
    @SuppressWarnings({ "unchecked" })
    public List<T> getOrderedAnswers() {
        List<T> answers = this.getAnswers();

        return ((answers != null) ? IteratorUtils.toList(ToolDnsRecordOrderUtils.buildOrderedIterator(this.recordType, answers)) : null);
    }

    @Override
    public Name getQuestionName() {
        return this.questionName;
    }

    @Override
    public boolean hasRawAnswers() {
        return !CollectionUtils.isEmpty(this.getRawAnswers());
    }

    @Nullable
    @Override
    public List<Record> getRawAnswers() {
        return (this.isSuccess() ? ToolArrayUtils.asList(this.lookup.getAnswers()) : null);
    }

    @Override
    public Class<T> getRecordClass() {
        return this.recordClass;
    }

    @Override
    public boolean hasRecordPredicate() {
        return (this.recordPredicate != null);
    }

    @Nullable
    @Override
    public Predicate<T> getRecordPredicate() {
        return this.recordPredicate;
    }

    @Override
    public DnsRecordType getRecordType() {
        return this.recordType;
    }

    @Override
    public List<String> getMessages() {
        return ToolArrayUtils.asList(this.lookup.getErrorString());
    }

    @Override
    public boolean isSuccess() {
        return this.getType().isSuccess();
    }

    @Override
    public DnsResultType getType() {
        return ToolDnsUtils.findByCode(DnsResultType.class, this.lookup.getResult());
    }
}
