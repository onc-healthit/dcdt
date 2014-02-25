package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.utils.ToolListUtils;
import org.xbill.DNS.SRVRecord;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public abstract class SrvRecordUtils {
    public static TreeMap<Integer, List<SRVRecord>> sortSrvRecords(List<SRVRecord> srvRecords) {
        TreeMap<Integer, List<SRVRecord>> srvRecordsPrioritized = sortSrvRecordsByPriority(srvRecords);

        for (int priority : srvRecordsPrioritized.keySet()) {
            List<SRVRecord> sortedSrvRecordsByWeight = new ArrayList<>();
            List<SRVRecord> tempSortedSrvRecordsByWeight = sortPrioritizedSrvRecordsByWeight(srvRecordsPrioritized.get(priority));

            while (tempSortedSrvRecordsByWeight.size() > 0) {
                sortSrvRecordsByWeight(sortedSrvRecordsByWeight, tempSortedSrvRecordsByWeight);
            }
            srvRecordsPrioritized.put(priority, sortedSrvRecordsByWeight);
        }
        return srvRecordsPrioritized;
    }

    public static List<SRVRecord> sortPrioritizedSrvRecordsByWeight(List<SRVRecord> records) {
        return ToolListUtils.sort(records, new Comparator<SRVRecord>() {
            @Override
            public int compare(SRVRecord record1, SRVRecord record2) {
                return Integer.compare(record1.getWeight(), record2.getWeight());
            }
        });
    }

    public static TreeMap<Integer, List<SRVRecord>> sortSrvRecordsByPriority(List<SRVRecord> srvRecords) {
        TreeMap<Integer, List<SRVRecord>> srvRecordsPrioritized = new TreeMap<>();

        for (SRVRecord srvRecord : srvRecords) {
            int priority = srvRecord.getPriority();

            if (srvRecordsPrioritized.containsKey(priority)) {
                srvRecordsPrioritized.get(priority).add(srvRecord);
            } else {
                List<SRVRecord> newSrvRecords = new ArrayList<>();
                newSrvRecords.add(srvRecord);
                srvRecordsPrioritized.put(priority, newSrvRecords);
            }
        }
        return srvRecordsPrioritized;
    }

    public static void sortSrvRecordsByWeight(List<SRVRecord> sortedSrvRecordsByWeight, List<SRVRecord> tempSortedSrvRecordsByWeight) {
        int[][] runningSum = getRunningSum(tempSortedSrvRecordsByWeight);
        int randomNum = (int) (Math.random() * sumWeights(tempSortedSrvRecordsByWeight));

        if (runningSum.length > 1) {
            for (int i = 0; i < runningSum.length; i++) {
                if (randomNum >= runningSum[i][1]) {
                    sortedSrvRecordsByWeight.add(tempSortedSrvRecordsByWeight.get(i));
                    tempSortedSrvRecordsByWeight.remove(i);
                    return;
                }
            }
        } else {
            sortedSrvRecordsByWeight.add(tempSortedSrvRecordsByWeight.get(0));
            tempSortedSrvRecordsByWeight.remove(0);
        }
    }

    public static int sumWeights(List<SRVRecord> srvRecords) {
        int sum = 0;

        for (SRVRecord srvRecord : srvRecords) {
            sum += srvRecord.getWeight();
        }
        return sum;
    }

    public static int[][] getRunningSum(List<SRVRecord> sortedSrvRecordsByWeight) {
        int[][] runningSum = new int[sortedSrvRecordsByWeight.size()][2];
        for (int i = 0; i < sortedSrvRecordsByWeight.size(); i++) {
            int weight = sortedSrvRecordsByWeight.get(i).getWeight();
            runningSum[i][0] = weight;
            if (i == 0) {
                runningSum[i][1] = weight;
            } else {
                runningSum[i][1] = runningSum[i - 1][1] + weight;
            }
        }
        return runningSum;
    }
}
