package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.SRVRecord;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Test(groups = { "dcdt.test.unit.dns.all", "dcdt.test.unit.dns.srv" })
public class SrvRecordUtilsUnitTests extends AbstractToolUnitTests {
    @Resource(name = "testSrvRecordConfigs")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<SrvRecordConfig> srvRecordConfigs;

    @Value("${dcdt.test.ldap.lookup.priority.1}")
    private int priority1;

    @Value("${dcdt.test.ldap.lookup.priority.2}")
    private int priority2;

    private List<SRVRecord> srvRecords;
    private TreeMap<Integer, List<SRVRecord>> srvRecordsPrioritized;

    @BeforeClass
    public void createSrvRecords() throws DnsException {
        this.srvRecords = new ArrayList<>(this.srvRecordConfigs.size());
        for (SrvRecordConfig srvRecordConfig : this.srvRecordConfigs) {
            srvRecords.add(srvRecordConfig.toRecord());
        }
    }

    @Test(dependsOnMethods = { "testSortSrvRecordsByPriority" })
    public void testSortSrvRecords() {
        TreeMap<Integer, List<SRVRecord>> srvRecordsBeforeSorting = new TreeMap<>(this.srvRecordsPrioritized);
        TreeMap<Integer, List<SRVRecord>> srvRecordsAfterSorting = SrvRecordUtils.sortSrvRecords(this.srvRecords);
        Assert.assertEquals(srvRecordsAfterSorting.size(), srvRecordsBeforeSorting.size());
        for (int priority : srvRecordsBeforeSorting.keySet()) {
            Assert.assertEquals(srvRecordsAfterSorting.get(priority).size(), srvRecordsBeforeSorting.get(priority).size());
        }
    }

    @Test
    public void testSortSrvRecordsByPriority() {
        this.srvRecordsPrioritized = new TreeMap<>();
        srvRecordsPrioritized.put(this.priority1, ToolArrayUtils.asList(this.srvRecords.get(0), this.srvRecords.get(1)));
        srvRecordsPrioritized.put(this.priority2, ToolArrayUtils.asList(this.srvRecords.get(2)));
        Assert.assertEquals(SrvRecordUtils.sortSrvRecordsByPriority(this.srvRecords), srvRecordsPrioritized);
    }

    @Test
    public void testSumWeights() {
        Assert.assertEquals(SrvRecordUtils.sumWeights(this.srvRecords), 200);
    }

    @Test(dependsOnMethods = { "testSortSrvRecordsByPriority" })
    public void testGetRunningSum() {
        Assert.assertEquals(SrvRecordUtils.getRunningSum(SrvRecordUtils.sortPrioritizedSrvRecordsByWeight(this.srvRecords)), new int[][] { { 0, 0 },
            { 100, 100 }, { 100, 200 } });
        Assert.assertEquals(SrvRecordUtils.getRunningSum(SrvRecordUtils.sortPrioritizedSrvRecordsByWeight(this.srvRecordsPrioritized.get(this.priority1))),
            new int[][] { { 0, 0 }, { 100, 100 } });
        Assert.assertEquals(SrvRecordUtils.getRunningSum(SrvRecordUtils.sortPrioritizedSrvRecordsByWeight(this.srvRecordsPrioritized.get(this.priority2))),
            new int[][] { { 100, 100 } });
    }

    @Test(dependsOnMethods = { "testSortSrvRecordsByPriority" })
    public void testSortSrvRecordsByWeight() {
        List<SRVRecord> sortedSrvRecordsByWeight = new ArrayList<>();
        List<SRVRecord> tempSortedSrvRecordsByWeight = SrvRecordUtils.sortPrioritizedSrvRecordsByWeight(this.srvRecordsPrioritized.get(this.priority1));
        SrvRecordUtils.sortSrvRecordsByWeight(sortedSrvRecordsByWeight, tempSortedSrvRecordsByWeight);
        Assert.assertEquals(sortedSrvRecordsByWeight.size(), 1);
        Assert.assertEquals(tempSortedSrvRecordsByWeight.size(), 1);

        SrvRecordUtils.sortSrvRecordsByWeight(sortedSrvRecordsByWeight, tempSortedSrvRecordsByWeight);
        Assert.assertEquals(sortedSrvRecordsByWeight.size(), 2);
        Assert.assertEquals(tempSortedSrvRecordsByWeight.size(), 0);
    }
}
