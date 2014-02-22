package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithm;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsRuntimeException;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import java.security.PublicKey;
import java.util.Collection;
import java.util.EnumSet;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.Transformer;
import org.xbill.DNS.DClass;
import org.xbill.DNS.DNSKEYRecord;
import org.xbill.DNS.DNSKEYRecord.Protocol;
import org.xbill.DNS.DNSSEC.DNSSECException;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public abstract class ToolDnsRecordUtils {
    public static class DnsRecordConfigAnswerTransformer<T extends Record> extends DnsRecordConfigTransformer<T> {
        private T record;

        @SuppressWarnings({ "unchecked" })
        public DnsRecordConfigAnswerTransformer(DnsRecordType recordType, Class<T> recordClass, T record) {
            super(recordType, recordClass);

            this.record = record;
        }

        @Nullable
        @Override
        @SuppressWarnings({ "unchecked" })
        public T transform(DnsRecordConfig<? extends Record> recordConfig) {
            return recordConfig.getName().equals(this.record.getName()) ? super.transform(recordConfig) : null;
        }
    }

    public static class DnsRecordConfigTransformer<T extends Record> implements Transformer<DnsRecordConfig<? extends Record>, T> {
        protected DnsRecordType recordType;
        protected Class<T> recordClass;

        public DnsRecordConfigTransformer(DnsRecordType recordType, Class<T> recordClass) {
            this.recordType = recordType;
            this.recordClass = recordClass;
        }

        @Nullable
        @Override
        @SuppressWarnings({ "unchecked" })
        public T transform(DnsRecordConfig<? extends Record> recordConfig) {
            try {
                return (recordConfig.getRecordType() == this.recordType) ? this.recordClass.cast(recordConfig.toRecord()) : null;
            } catch (DnsException e) {
                throw new DnsRuntimeException(String.format("Unable to transform DNS record configuration (class=%s, type=%s) into DNS record (class=%s).",
                    ToolClassUtils.getName(recordConfig), recordConfig.getRecordType().getTypeDisplay(),
                    ToolClassUtils.getName(recordConfig.getRecordType().getRecordClass())), e);
            }
        }
    }

    @Nonnegative
    public static int getKeyTag(DnsKeyAlgorithm dnsKeyAlg, PublicKey publicKey) throws DnsException {
        try {
            return new DNSKEYRecord(Name.root, DClass.IN, 0, 0, Protocol.DNSSEC, dnsKeyAlg.getAlgorithm(), publicKey).getFootprint();
        } catch (DNSSECException e) {
            throw new DnsException(String.format("Unable to get key tag for public key (dnsAlg=%s, class=%s).", dnsKeyAlg.name(),
                ToolClassUtils.getName(publicKey)), e);
        }
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Record> Collection<T> findAnswers(T questionRecord,
        @Nullable Iterable<? extends DnsRecordConfig<? extends Record>> ... recordConfigIterables) {
        return findAnswers(questionRecord, ToolArrayUtils.asList(recordConfigIterables));
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Record> Collection<T> findAnswers(T questionRecord,
        @Nullable Iterable<? extends Iterable<? extends DnsRecordConfig<? extends Record>>> recordConfigIterables) {
        DnsRecordType questionRecordType = ToolDnsRecordUtils.findByType(questionRecord.getType());

        return CollectionUtils.emptyIfNull(((questionRecordType != null) ? CollectionUtils.select(CollectionUtils.collect(
            ToolIteratorUtils.chainedIterator(recordConfigIterables),
            new DnsRecordConfigAnswerTransformer<>(questionRecordType, (Class<T>) questionRecordType.getRecordClass(), questionRecord)), PredicateUtils
            .notNullPredicate()) : null));
    }

    @Nullable
    public static DnsRecordType findByType(int recordType) {
        for (DnsRecordType enumItem : EnumSet.allOf(DnsRecordType.class)) {
            if (enumItem.getType() == recordType) {
                return enumItem;
            }
        }

        return null;
    }
}
