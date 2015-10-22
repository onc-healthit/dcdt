package gov.hhs.onc.dcdt.service.http;

import gov.hhs.onc.dcdt.http.lookup.HttpLookupService;
import gov.hhs.onc.dcdt.http.utils.ToolHttpUtils;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import java.net.URI;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-http.xml", "spring/spring-service-http-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.http" })
public class HttpServiceFunctionalTests extends AbstractToolServiceFunctionalTests<HttpService> {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServiceFunctionalTests.class);

    @Resource(name = "httpLookupServiceLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HttpLookupService lookupService;

    public HttpServiceFunctionalTests() {
        super(HttpService.class);
    }

    @Test
    public void testGetUris() throws Exception {
        Assert.assertTrue(this.service.hasServers(), "HTTP service does not have any HTTP server(s).");

        HttpServerConfig serverConfig;

        // noinspection ConstantConditions
        for (HttpServer server : this.service.getServers()) {
            // noinspection ConstantConditions
            lookupService.getUri(new URI(ToolHttpUtils.HTTP_SCHEME, null, (serverConfig = server.getConfig()).getHost(true).getHostAddress(), serverConfig
                .getPort(), "/csr/test.direct-test.com_ca_root.csr", null, null));
        }
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.http" })
    @Override
    public void startService() {
        super.startService();
    }
}
