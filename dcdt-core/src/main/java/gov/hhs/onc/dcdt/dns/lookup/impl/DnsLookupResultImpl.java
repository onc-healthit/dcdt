package gov.hhs.onc.dcdt.dns.lookup.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLookupResultBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordOrderUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public class DnsLookupResultImpl<T extends Record> extends AbstractToolLookupResultBean implements DnsLookupResult<T> {
    private List<Name> aliases;
    private List<T> answers;
    private List<ToolMessage> msgs = new ArrayList<>();
    private List<T> orderedAnswers;
    private Name questionName;
    private List<Record> rawAnswers;
    private Class<T> recordClass;
    private DnsRecordType recordType;
    private DnsResultType type;

    public DnsLookupResultImpl(DnsRecordType recordType, Class<T> recordClass, Name questionName, DnsResultType type) {
        this(recordType, recordClass, questionName, type, null, null);
    }

    public DnsLookupResultImpl(DnsRecordType recordType, Class<T> recordClass, Name questionName, DnsResultType type, @Nullable List<Name> aliases,
        @Nullable List<Record> rawAnswers) {
        this.recordType = recordType;
        this.recordClass = recordClass;
        this.questionName = questionName;
        this.type = type;
        this.aliases = aliases;
        this.rawAnswers = rawAnswers;

        if (this.isSuccess() && (this.rawAnswers != null)) {
            this.answers = ToolStreamUtils.asInstances(this.rawAnswers.stream(), this.recordClass).collect(Collectors.toList());
            this.orderedAnswers = IteratorUtils.toList(ToolDnsRecordOrderUtils.buildOrderedIterator(this.recordType, answers));
        }
    }

    @Override
    public boolean hasAliases() {
        return !CollectionUtils.isEmpty(this.aliases);
    }

    @Nullable
    @Override
    public List<Name> getAliases() {
        return this.aliases;
    }

    @Override
    public boolean hasAnswers() {
        return !CollectionUtils.isEmpty(this.answers);
    }

    @Nullable
    @Override
    public List<T> getAnswers() {
        return this.answers;
    }

    @Override
    public boolean hasOrderedAnswers() {
        return !CollectionUtils.isEmpty(this.orderedAnswers);
    }

    @Nullable
    @Override
    public List<T> getOrderedAnswers() {
        return this.orderedAnswers;
    }

    @Override
    public Name getQuestionName() {
        return this.questionName;
    }

    @Override
    public boolean hasRawAnswers() {
        return !CollectionUtils.isEmpty(this.rawAnswers);
    }

    @Nullable
    @Override
    public List<Record> getRawAnswers() {
        return this.rawAnswers;
    }

    @Override
    public Class<T> getRecordClass() {
        return this.recordClass;
    }

    @Override
    public DnsRecordType getRecordType() {
        return this.recordType;
    }

    @Override
    public List<ToolMessage> getMessages() {
        return this.msgs;
    }

    @Override
    public boolean isSuccess() {
        return this.getType().isSuccess();
    }

    @Override
    public DnsResultType getType() {
        return this.type;
    }
}
