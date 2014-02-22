package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils.DnsRecordConfigTransformer;
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
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;

@ContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.dns" })
public class DnsServiceFunctionalTests extends AbstractToolServiceFunctionalTests<DnsService> {
    private final static String BEAN_NAME_DNS_LOOKUP_SERVICE_PROTO = "dnsLookupServiceImpl";

    private Map<DnsServerConfig, DnsLookupService> testDnsServerConfigLookupServices;

    public DnsServiceFunctionalTests() {
        super(DnsService.class);
    }

    @Test
    public void testLookupDnsRecords() throws Exception {
        DnsLookupService testDnsServerConfigLookupService;
        Map<DnsRecordType, List<? extends DnsRecordConfig<? extends Record>>> testDnsConfigRecordConfigsMap;
        Collection<? extends Record> testDnsConfigRecords, testDnsConfigRecordsLookup;

        for (DnsServerConfig testDnsServerConfig : this.testDnsServerConfigLookupServices.keySet()) {
            if (!testDnsServerConfig.hasDnsConfigs()) {
                continue;
            }

            testDnsServerConfigLookupService = this.testDnsServerConfigLookupServices.get(testDnsServerConfig);

            // noinspection ConstantConditions
            for (InstanceDnsConfig testDnsConfig : testDnsServerConfig.getDnsConfigs()) {
                for (DnsRecordType testDnsConfigRecordType : (testDnsConfigRecordConfigsMap = testDnsConfig.mapRecordConfigs()).keySet()) {
                    for (Record testDnsConfigRecord : (testDnsConfigRecords =
                        CollectionUtils.collect(testDnsConfigRecordConfigsMap.get(testDnsConfigRecordType), new DnsRecordConfigTransformer<>(
                            testDnsConfigRecordType, testDnsConfigRecordType.getRecordClass())))) {
                        Assert.assertEqualsNoOrder(ToolCollectionUtils.toArray(
                            (testDnsConfigRecordsLookup =
                                testDnsServerConfigLookupService.getRecords(testDnsConfigRecordType.getRecordClass(), testDnsConfigRecordType,
                                    testDnsConfigRecord.getName())), Record.class), ToolCollectionUtils.toArray(testDnsConfigRecords, Record.class), String
                            .format("DNS record(s) do not match: actual=[%s], expected=[%s]", ToolStringUtils.joinDelimit(testDnsConfigRecords, ", "),
                                ToolStringUtils.joinDelimit(testDnsConfigRecordsLookup, ", ")));
                    }
                }
            }
        }
    }

    @BeforeClass(dependsOnMethods = { "startService" }, groups = { "dcdt.test.func.service.dns" })
    public void buildDnsLookupServices() throws Exception {
        Assert.assertTrue(this.service.hasServers(), "DNS service does not have any DNS server(s).");

        List<DnsServer> dnsServers = this.service.getServers();
        DnsServerConfig dnsServerConfig;
        Resolver dnsLookupResolver;

        // noinspection ConstantConditions
        this.testDnsServerConfigLookupServices = new HashMap<>(dnsServers.size());

        // noinspection ConstantConditions
        for (DnsServer dnsServer : dnsServers) {
            dnsLookupResolver = new ExtendedResolver(ArrayUtils.toArray((dnsServerConfig = dnsServer.getConfig()).getBindAddress().getHostAddress()));
            dnsLookupResolver.setPort(dnsServerConfig.getBindPort());

            this.testDnsServerConfigLookupServices.put(dnsServerConfig,
                ToolBeanFactoryUtils.createBean(this.applicationContext, BEAN_NAME_DNS_LOOKUP_SERVICE_PROTO, DnsLookupService.class, dnsLookupResolver));
        }
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.dns" })
    @Override
    public void startService() {
        super.startService();
    }
}
