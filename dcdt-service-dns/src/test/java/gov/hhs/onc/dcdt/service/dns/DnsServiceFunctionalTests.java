package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils.DnsRecordConfigTransformer;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsResolverUtils;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.Record;

@ContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.dns" })
public class DnsServiceFunctionalTests extends AbstractToolServiceFunctionalTests<DnsService> {
    private Map<DnsServerConfig, DnsLookupService> testDnsServerConfigLookupServices;

    public DnsServiceFunctionalTests() {
        super(DnsService.class);
    }

    @Test
    public void testLookupDnsRecords() throws Exception {
        DnsLookupService testDnsServerConfigLookupService;
        Map<DnsRecordType, List<? extends DnsRecordConfig<? extends Record>>> testDnsConfigRecordConfigsMap;
        Collection<? extends Record> testDnsConfigAnswerRecords;
        DnsLookupResult<? extends Record> testDnsLookupResult;

        for (DnsServerConfig testDnsServerConfig : this.testDnsServerConfigLookupServices.keySet()) {
            if (!testDnsServerConfig.hasDnsConfigs()) {
                continue;
            }

            testDnsServerConfigLookupService = this.testDnsServerConfigLookupServices.get(testDnsServerConfig);

            // noinspection ConstantConditions
            for (InstanceDnsConfig testDnsConfig : testDnsServerConfig.getDnsConfigs()) {
                for (DnsRecordType testDnsConfigRecordType : (testDnsConfigRecordConfigsMap = testDnsConfig.mapRecordConfigs()).keySet()) {
                    for (Record testDnsConfigRecord : CollectionUtils.collect(testDnsConfigRecordConfigsMap.get(testDnsConfigRecordType),
                        new DnsRecordConfigTransformer<>(testDnsConfigRecordType, testDnsConfigRecordType.getRecordClass()))) {
                        // noinspection ConstantConditions
                        Assert.assertEqualsNoOrder(
                            ToolCollectionUtils.toArray(
                                (testDnsLookupResult =
                                    testDnsServerConfigLookupService.lookupRecords(testDnsConfigRecordType, testDnsConfigRecordType.getRecordClass(),
                                        testDnsConfigRecord.getName())).getResolvedAnswers(), Record.class),
                            ToolCollectionUtils.toArray(
                                (testDnsConfigAnswerRecords =
                                    ToolCollectionUtils.nullIfEmpty(testDnsServerConfig.findAuthoritativeDnsConfig(testDnsConfigRecord).findAnswers(
                                        testDnsConfigRecord))), Record.class),
                            String.format("DNS record(s) do not match: expected=[%s], actual=[%s]",
                                ToolStringUtils.joinDelimit(testDnsConfigAnswerRecords, ", "),
                                ToolStringUtils.joinDelimit(testDnsLookupResult.getAnswers(), ", ")));
                    }
                }
            }
        }
    }

    @BeforeClass(dependsOnMethods = { "startService" }, groups = { "dcdt.test.func.service.dns" })
    public void buildDnsLookupServices() throws Exception {
        Assert.assertTrue(this.service.hasServers(), "DNS service does not have any DNS server(s).");

        List<DnsServer> dnsServers = this.service.getServers();

        // noinspection ConstantConditions
        this.testDnsServerConfigLookupServices = new HashMap<>(dnsServers.size());

        DnsServerConfig dnsServerConfig;
        DnsLookupService dnsServerConfigLookupService;

        // noinspection ConstantConditions
        for (DnsServer dnsServer : dnsServers) {
            dnsServerConfigLookupService = ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DnsLookupService.class);
            // noinspection ConstantConditions
            dnsServerConfigLookupService.setResolver(ToolDnsResolverUtils.fromSocketAddress((dnsServerConfig = dnsServer.getConfig()).toSocketAddress()));

            this.testDnsServerConfigLookupServices.put(dnsServerConfig, dnsServerConfigLookupService);
        }
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.dns" })
    @Override
    public void startService() {
        super.startService();
    }
}
