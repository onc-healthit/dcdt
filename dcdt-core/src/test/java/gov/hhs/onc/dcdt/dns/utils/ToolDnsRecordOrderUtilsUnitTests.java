package gov.hhs.onc.dcdt.dns.utils;

import gov.hhs.onc.dcdt.collections.ToolTransformer;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.SRVRecord;

@Test(groups = { "dcdt.test.unit.dns.all", "dcdt.test.unit.dns.utils.all", "dcdt.test.unit.dns.utils.record.order" })
public class ToolDnsRecordOrderUtilsUnitTests extends AbstractToolUnitTests {
    @Resource(name = "testSrvRecordConfigs")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<SrvRecordConfig> srvRecordConfigs;

    @Value("${dcdt.test.ldap.lookup.priority.1}")
    private int priority1;

    @Value("${dcdt.test.ldap.lookup.priority.2}")
    private int priority2;

    private List<SRVRecord> srvRecords;

    @Test
    public void testBuildSrvRecordIterator() {
        List<SRVRecord> srvRecordsOrdered = IteratorUtils.toList(ToolDnsRecordOrderUtils.buildSrvRecordIterator(this.srvRecords));
        Assert.assertEquals(this.srvRecords.size(), srvRecordsOrdered.size());
        Assert.assertEquals(srvRecordsOrdered.get(0).getPriority(), Math.min(this.priority1, this.priority2));
    }

    @BeforeClass
    public void createSrvRecords() throws DnsException {
        this.srvRecords = (List<SRVRecord>) ToolStreamUtils.transform(this.srvRecordConfigs, ToolTransformer.wrap(SrvRecordConfig::toRecord));
    }
}
