package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.utils.ToolListUtils;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class DnsRecordSortingUtils {
    public static class PriorityOrderedComparator implements Comparator<Integer>, Serializable {
        private final static long serialVersionUID = 0L;

        @Override
        public int compare(Integer priority1, Integer priority2) {
            return Integer.compare(priority1, priority2);
        }
    }

    public static <T extends Record> Map<Integer, List<T>> sortSrvRecordsByPriority(List<T> records) {
        Map<Integer, List<T>> prioritizedRecords = new TreeMap<>(new PriorityOrderedComparator());

        for (T record : records) {
            int priority;
            if (record instanceof SRVRecord) {
                priority = ((SRVRecord) record).getPriority();
            } else if (record instanceof MXRecord) {
                priority = ((MXRecord) record).getPriority();
            } else {
                return prioritizedRecords;
            }

            if (prioritizedRecords.containsKey(priority)) {
                prioritizedRecords.get(priority).add(record);
            } else {
                List<T> newRecords = new ArrayList<>();
                newRecords.add(record);
                prioritizedRecords.put(priority, newRecords);
            }
        }
        return prioritizedRecords;
    }

    public static class SrvRecordSortingUtils {
        /**
         * Algorithm for ordering SRV records of the same priority found in: <a href="http://tools.ietf.org/html/rfc2782#page-3">RFC 2782 - A DNS RR for
         * specifying the location of services (DNS SRV) (page 3)</a>
         */

        public static class WeightOrderedRecordComparator implements Comparator<SRVRecord>, Serializable {
            private final static long serialVersionUID = 0L;

            @Override
            public int compare(SRVRecord record1, SRVRecord record2) {
                return Integer.compare(record1.getWeight(), record2.getWeight());
            }
        }

        public static Map<Integer, List<SRVRecord>> sortSrvRecords(List<SRVRecord> srvRecords) {
            Map<Integer, List<SRVRecord>> srvRecordsPrioritized = sortSrvRecordsByPriority(srvRecords);

            for (int priority : srvRecordsPrioritized.keySet()) {
                srvRecordsPrioritized.put(priority,
                    sortSrvRecordsByWeight(ToolListUtils.sort(srvRecordsPrioritized.get(priority), new WeightOrderedRecordComparator())));
            }

            return srvRecordsPrioritized;
        }

        public static List<SRVRecord> sortSrvRecordsByWeight(List<SRVRecord> srvRecords) {
            List<SRVRecord> sortedSrvRecordsByWeight = new ArrayList<>();

            while (!srvRecords.isEmpty()) {
                int randomNum = (int) (Math.random() * sumWeights(srvRecords));
                int runningSum = 0;

                for (int i = 0; i < srvRecords.size(); i++) {
                    SRVRecord record = srvRecords.get(i);
                    runningSum += record.getWeight();

                    if (runningSum >= randomNum) {
                        sortedSrvRecordsByWeight.add(record);
                        srvRecords.remove(i);
                        break;
                    }
                }
            }

            return sortedSrvRecordsByWeight;
        }

        public static int sumWeights(List<SRVRecord> srvRecords) {
            int sum = 0;

            for (SRVRecord srvRecord : srvRecords) {
                sum += srvRecord.getWeight();
            }
            return sum;
        }
    }
}
