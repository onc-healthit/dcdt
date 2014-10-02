package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsDclassType;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsSpfStrings;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.CERTRecord;
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
        protected T transformInternal(DnsRecordConfig<? extends Record> recordConfig) throws Exception {
            T recordTransformed = super.transformInternal(recordConfig);

            return (((recordTransformed != null) && Objects.equals(recordTransformed.getName(), this.record.getName())) ? recordTransformed : null);
        }
    }

    public static class DnsRecordConfigTransformer<T extends Record> extends AbstractToolTransformer<DnsRecordConfig<? extends Record>, T> {
        protected DnsRecordType recordType;
        protected Class<T> recordClass;

        public DnsRecordConfigTransformer(DnsRecordType recordType, Class<T> recordClass) {
            this.recordType = recordType;
            this.recordClass = recordClass;
        }

        @Nullable
        @Override
        @SuppressWarnings({ "unchecked" })
        protected T transformInternal(DnsRecordConfig<? extends Record> recordConfig) throws Exception {
            return (recordConfig.getRecordType() == this.recordType) ? this.recordClass.cast(recordConfig.toRecord()) : null;
        }
    }

    public static class CertRecordParameterPredicate extends AbstractToolPredicate<CERTRecord> {
        public final static CertRecordParameterPredicate INSTANCE_PKIX = new CertRecordParameterPredicate(DnsCertificateType.PKIX);

        private DnsCertificateType certType;
        private Set<DnsKeyAlgorithmType> keyAlgTypes;

        public CertRecordParameterPredicate(@Nullable DnsCertificateType certType) {
            this(certType, null);
        }

        public CertRecordParameterPredicate(@Nullable DnsCertificateType certType, @Nullable Set<DnsKeyAlgorithmType> keyAlgTypes) {
            this.certType = certType;
            this.keyAlgTypes = keyAlgTypes;
        }

        @Override
        protected boolean evaluateInternal(CERTRecord certRecord) throws Exception {
            return (((this.keyAlgTypes == null) || this.keyAlgTypes.contains(ToolDnsUtils.findByCode(DnsKeyAlgorithmType.class, certRecord.getAlgorithm()))) && ((this.certType == null) || (certRecord
                .getCertType() == this.certType.getCode())));
        }
    }

    public static class DnsRecordDataStringTransformer extends AbstractToolTransformer<Record, String> {
        public final static DnsRecordDataStringTransformer INSTANCE = new DnsRecordDataStringTransformer();

        @Override
        protected String transformInternal(Record record) throws Exception {
            return record.rdataToString();
        }
    }

    public static class AuthoritativeDnsConfigPredicate extends AbstractToolPredicate<InstanceDnsConfig> {
        private Record questionRecord;

        public AuthoritativeDnsConfigPredicate(Record questionRecord) {
            this.questionRecord = questionRecord;
        }

        @Override
        protected boolean evaluateInternal(InstanceDnsConfig dnsConfig) throws Exception {
            return dnsConfig.isAuthoritative(this.questionRecord);
        }
    }

    public final static DateFormat DATE_FORMAT_SERIAL = new SimpleDateFormat("yyyyMMdd");

    public static String buildSpf(String ... spfStrs) {
        return buildSpf(ToolArrayUtils.asList(spfStrs));
    }

    public static String buildSpf(List<String> spfStrs) {
        if (!Objects.equals(ToolListUtils.getFirst(spfStrs), DnsSpfStrings.MOD_VERSION_1)) {
            ToolListUtils.addFirst(spfStrs, DnsSpfStrings.MOD_VERSION_1);
        }

        if (!Objects.equals(ToolListUtils.getLast(spfStrs), DnsSpfStrings.MECH_ALL_FAIL)) {
            spfStrs.add(DnsSpfStrings.MECH_ALL_FAIL);
        }

        // noinspection ConstantConditions
        return ToolStringUtils.joinDelimit(spfStrs, DnsSpfStrings.DELIM);
    }

    @Nonnegative
    public static int generateSerial() {
        return generateSerial(0);
    }

    @Nonnegative
    public static int generateSerial(@Nonnegative int seqNum) {
        return Integer.parseInt(DATE_FORMAT_SERIAL.format(new Date()) + StringUtils.leftPad(Integer.toString(seqNum), 2, Integer.toString(0)));
    }

    @Nonnegative
    public static int getKeyTag(DnsKeyAlgorithmType keyAlgType, PublicKey publicKey) throws DnsException {
        try {
            return new DNSKEYRecord(Name.root, DnsDclassType.IN.getCode(), 0, 0, Protocol.DNSSEC, keyAlgType.getCode(), publicKey).getFootprint();
        } catch (DNSSECException e) {
            throw new DnsException(String.format("Unable to get key tag for public key (class=%s, algType=%s).", ToolClassUtils.getName(publicKey),
                keyAlgType.name()), e);
        }
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends Record> Collection<T> findAnswers(T questionRecord,
        @Nullable Iterable<? extends DnsRecordConfig<? extends Record>> ... recordConfigs) {
        return findAnswers(questionRecord, ToolArrayUtils.asList(recordConfigs));
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Record> Collection<T> findAnswers(T questionRecord,
        @Nullable Iterable<? extends Iterable<? extends DnsRecordConfig<? extends Record>>> recordConfigs) {
        DnsRecordType questionRecordType = ToolDnsUtils.findByCode(DnsRecordType.class, questionRecord.getType());

        // noinspection ConstantConditions
        return CollectionUtils.emptyIfNull(((questionRecordType != null) ? CollectionUtils.select(CollectionUtils.collect(
            ToolIteratorUtils.chainedIterator(recordConfigs),
            new DnsRecordConfigAnswerTransformer<>(questionRecordType, ((Class<T>) questionRecordType.getRecordClass()), questionRecord)), PredicateUtils
            .notNullPredicate()) : null));
    }
}
