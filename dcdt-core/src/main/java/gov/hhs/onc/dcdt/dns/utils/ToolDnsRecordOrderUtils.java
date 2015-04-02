package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;

/**
 * Algorithm for ordering SRV records of the same priority found in: <a href="http://tools.ietf.org/html/rfc2782#page-3">RFC 2782 - A DNS RR for specifying the
 * location of services (DNS SRV) (page 3)</a>
 */
public abstract class ToolDnsRecordOrderUtils {
    private static class SrvRecordWeightedIterator implements Iterator<SRVRecord> {
        private Map<Integer, List<SRVRecord>> recordPriorityMap;
        private Iterator<Entry<Integer, List<SRVRecord>>> recordPriorityEntryIterator;
        private Entry<Integer, List<SRVRecord>> recordPriorityEntry;

        public SrvRecordWeightedIterator(Map<Integer, List<SRVRecord>> recordPriorityMap) {
            this.recordPriorityMap = recordPriorityMap;
            this.recordPriorityEntryIterator = this.recordPriorityMap.entrySet().iterator();
        }

        @Override
        public void remove() {
        }

        @Nullable
        @Override
        @SuppressWarnings({ "unchecked" })
        public SRVRecord next() {
            if (!this.hasNext()) {
                return null;
            }

            if (this.recordPriorityEntry == null) {
                this.recordPriorityEntry = this.recordPriorityEntryIterator.next();

                ToolListUtils.sort(this.recordPriorityEntry.getValue(), Comparator.comparing(SRVRecord::getWeight, Comparator.naturalOrder()));
            }

            List<SRVRecord> records = this.recordPriorityEntry.getValue();
            List<Integer> recordWeights = records.stream().map(SRVRecord::getWeight).collect(Collectors.toList());

            int recordWeightSum = recordWeights.stream().reduce(0, Integer::sum);
            int recordWeightSelect = new Random().nextInt(recordWeightSum + 1), recordWeightRunningSum = 0;
            SRVRecord record = null;

            for (int a = 0; a < recordWeights.size(); a++) {
                if ((recordWeightRunningSum += recordWeights.get(a)) >= recordWeightSelect) {
                    record = records.remove(a);

                    break;
                }
            }

            if (records.isEmpty()) {
                this.recordPriorityEntry = null;
            }

            return record;
        }

        @Override
        public boolean hasNext() {
            return ((this.recordPriorityEntry != null) || this.recordPriorityEntryIterator.hasNext());
        }
    }

    private static <T extends Record> Pair<Integer, T> transformDnsRecordPriorityPair(T record) {
        return new ImmutablePair<>((ToolClassUtils.isAssignable(record.getClass(), MXRecord.class)
            ? ((MXRecord) record).getPriority() : ((SRVRecord) record).getPriority()), record);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Record> Iterator<T> buildOrderedIterator(DnsRecordType recordType, Iterable<T> records) {
        switch (recordType) {
            case MX:
                return ((Iterator<T>) buildMxRecordIterator(((Iterable<MXRecord>) records)));

            case SRV:
                return ((Iterator<T>) buildSrvRecordIterator(((Iterable<SRVRecord>) records)));

            default:
                return records.iterator();
        }
    }

    public static Iterator<MXRecord> buildMxRecordIterator(Iterable<MXRecord> records) {
        return ToolIteratorUtils.chainedIterator(buildRecordPriorityMap(records).values());
    }

    public static Iterator<SRVRecord> buildSrvRecordIterator(Iterable<SRVRecord> records) {
        return new SrvRecordWeightedIterator(buildRecordPriorityMap(records));
    }

    private static <T extends Record> Map<Integer, List<T>> buildRecordPriorityMap(Iterable<T> records) {
        Map<Integer, List<T>> recordPriorityMap = new TreeMap<>();
        Integer recordPriority;

        for (Pair<Integer, T> recordPriorityPair : ToolStreamUtils.transform(records, ToolDnsRecordOrderUtils::transformDnsRecordPriorityPair)) {
            recordPriorityMap.putIfAbsent(recordPriority = recordPriorityPair.getKey(), new ArrayList<>());
            recordPriorityMap.get(recordPriority).add(recordPriorityPair.getValue());
        }

        return recordPriorityMap;
    }
}
