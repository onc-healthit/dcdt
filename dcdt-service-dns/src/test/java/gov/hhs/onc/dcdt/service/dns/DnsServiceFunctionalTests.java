package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.ToolRuntimeException;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsResolverUtils;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.Record;

@ContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.dns" })
public class DnsServiceFunctionalTests extends AbstractToolServiceFunctionalTests<DnsService> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DnsServiceFunctionalTests.class);

    @Value("${dcdt.test.func.service.dns.lookup.concurrent.threads}")
    private int lookupConcurrentNumThreads;

    private Map<DnsServerConfig, DnsLookupService> serverLookupServiceMap;

    public DnsServiceFunctionalTests() {
        super(DnsService.class);
    }

    @Test(dependsOnMethods = { "testLookupDnsRecords" })
    public void testLookupDnsRecordsConcurrent() throws Exception {
        final ThreadPoolTaskExecutor taskExec = new ThreadPoolTaskExecutor();
        taskExec.setDaemon(true);
        taskExec.setCorePoolSize(this.lookupConcurrentNumThreads);
        taskExec.initialize();

        final DnsServerConfig serverConfig = this.serverLookupServiceMap.keySet().iterator().next();
        // noinspection ConstantConditions
        final DnsRecordConfig<? extends Record> recordConfig = serverConfig.getConfigs().get(0).getARecordConfigs().get(0);
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CyclicBarrier stopBarrier = new CyclicBarrier((this.lookupConcurrentNumThreads + 1));

        for (int a = 0; a < this.lookupConcurrentNumThreads; a++) {
            final int taskId = a;

            taskExec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        startLatch.await();

                        // noinspection ConstantConditions
                        DnsServiceFunctionalTests.this.assertLookupAnswerRecordsMatch(serverConfig, recordConfig);

                        DnsServiceFunctionalTests.LOGGER.trace(String.format("Concurrent DNS lookup task (id=%d) completed (total=%d).", taskId,
                            stopBarrier.getNumberWaiting()));
                    } catch (Exception e) {
                        throw new ToolRuntimeException(String.format("Unable to execute concurrent DNS lookup task (id=%d).", taskId), e);
                    } finally {
                        try {
                            stopBarrier.await();
                        } catch (Exception ignored) {
                        }
                    }
                }
            });
        }

        startLatch.countDown();

        stopBarrier.await();
    }

    @Test
    public void testLookupDnsRecords() throws Exception {
        for (DnsServerConfig serverConfig : this.serverLookupServiceMap.keySet()) {
            if (!serverConfig.hasConfigs()) {
                continue;
            }

            // noinspection ConstantConditions
            for (InstanceDnsConfig config : serverConfig.getConfigs()) {
                for (DnsRecordConfig<? extends Record> recordConfig : IteratorUtils.asIterable(ToolIteratorUtils.chainedIterator(ToolArrayUtils.asList(
                    config.getARecordConfigs(), config.getCertRecordConfigs(), config.getCnameRecordConfigs(), config.getMxRecordConfigs(),
                    config.getNsRecordConfigs(), config.getPtrRecordConfigs(), ToolArrayUtils.asList(config.getSoaRecordConfig()),
                    config.getSrvRecordConfigs(), config.getTxtRecordConfigs())))) {
                    this.assertLookupAnswerRecordsMatch(serverConfig, recordConfig);
                }
            }
        }
    }

    @BeforeClass(dependsOnMethods = { "startService" }, groups = { "dcdt.test.func.service.dns" })
    public void buildServerLookupServices() throws Exception {
        Assert.assertTrue(this.service.hasServers(), "DNS service does not have any DNS server(s).");

        List<DnsServer> servers = this.service.getServers();

        // noinspection ConstantConditions
        this.serverLookupServiceMap = new HashMap<>(servers.size());

        DnsServerConfig serverConfig;
        DnsLookupService serverLookupService;

        // noinspection ConstantConditions
        for (DnsServer server : servers) {
            serverLookupService = ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DnsLookupService.class);
            // noinspection ConstantConditions
            serverLookupService.setResolver(ToolDnsResolverUtils.fromSocketAddress((serverConfig = server.getConfig()).toSocketAddress()));

            this.serverLookupServiceMap.put(serverConfig, serverLookupService);
        }
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.dns" })
    @Override
    public void startService() {
        super.startService();
    }

    @SuppressWarnings({ "unchecked" })
    private void assertLookupAnswerRecordsMatch(DnsServerConfig serverConfig, DnsRecordConfig<? extends Record> recordConfig) throws Exception {
        DnsRecordType recordType = recordConfig.getRecordType();
        Record record = recordConfig.toRecord();
        // noinspection ConstantConditions
        DnsLookupResult<? extends Record> lookupResult =
            this.serverLookupServiceMap.get(serverConfig).lookupRecords(recordType, recordType.getRecordClass(), record.getName());
        List<InstanceDnsConfig> authoritativeConfigs = serverConfig.findAuthoritativeConfigs(record);
        // noinspection ConstantConditions
        Collection<Record> configAnswerRecords = new ArrayList<>(authoritativeConfigs.size()), answerRecords = ((Collection<Record>) lookupResult.getAnswers());

        for (InstanceDnsConfig authoritativeConfig : authoritativeConfigs) {
            // noinspection ConstantConditions
            ToolCollectionUtils.addAll(configAnswerRecords, authoritativeConfig.findAnswers(record));
        }

        Assert.assertEqualsNoOrder(ToolCollectionUtils.toArray(answerRecords, Record.class), ToolCollectionUtils.toArray(configAnswerRecords, Record.class),
            String.format("DNS lookup result (type=%s) answer record(s) do not match: expected=[%s], actual=[%s]", lookupResult.getType().name(),
                ToolStringUtils.joinDelimit(configAnswerRecords, ", "), ToolStringUtils.joinDelimit(answerRecords, ", ")));
    }
}
