package gov.hhs.onc.dcdt.dns.lookup.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsResolverUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public class DnsLookupResultImpl<T extends Record> implements DnsLookupResult<T> {
    private DnsRecordType recordType;
    private Class<T> recordClass;
    private Name questionName;
    private List<T> answerRecords;
    private String errorStr;
    private DnsResultType type;

    @SuppressWarnings({ "unchecked" })
    public DnsLookupResultImpl(DnsRecordType recordType, Class<T> recordClass, Name questionName, Lookup lookup) throws DnsException {
        this.recordType = recordType;
        this.recordClass = recordClass;
        this.questionName = questionName;
        this.type = ToolDnsResolverUtils.findResultType(lookup.getResult());

        if (!this.type.isError()) {
            this.answerRecords = ToolCollectionUtils.addAll(new ArrayList<T>(), ToolArrayUtils.asList(((T[]) lookup.getAnswers())));
        } else {
            this.errorStr = lookup.getErrorString();
        }
    }

    @Override
    public boolean hasAnswers() {
        return !CollectionUtils.isEmpty(this.answerRecords);
    }

    @Nullable
    @Override
    public List<T> getAnswers() {
        return this.answerRecords;
    }

    @Override
    public boolean hasErrorString() {
        return !StringUtils.isBlank(this.errorStr);
    }

    @Nullable
    @Override
    public String getErrorString() {
        return this.errorStr;
    }

    @Override
    public Name getQuestionName() {
        return this.questionName;
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
    public DnsResultType getType() {
        return this.type;
    }
}
