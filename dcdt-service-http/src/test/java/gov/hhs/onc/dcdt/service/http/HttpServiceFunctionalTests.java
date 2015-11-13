package gov.hhs.onc.dcdt.service.http;

import gov.hhs.onc.dcdt.http.lookup.HttpLookupResult;
import gov.hhs.onc.dcdt.http.lookup.HttpLookupService;
import gov.hhs.onc.dcdt.http.utils.ToolHttpUtils;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import java.net.URI;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-http.xml", "spring/spring-service-http-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.http" })
public class HttpServiceFunctionalTests extends AbstractToolServiceFunctionalTests<HttpServerConfig, HttpServer, HttpService> {
    @Resource(name = "httpLookupServiceCombined")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HttpLookupService lookupService;

    public HttpServiceFunctionalTests() {
        super(HttpService.class);
    }

    @Test
    public void testGetUris() throws Exception {
        Assert.assertTrue(this.service.hasServers(), "HTTP service does not have any HTTP server(s).");

        HttpServerConfig serverConfig;
        String serverHostAddr;
        int serverPort;
        Map<String, DiscoveryTestcaseCredential> discoveryTestcaseCredItemPaths;
        URI discoveryTestcaseIssuerCredItemUri;
        HttpLookupResult discoveryTestcaseIssuerCredItemLookupResult;
        DiscoveryTestcaseCredential discoveryTestcaseCred;
        String discoveryTestcaseCredName;

        // noinspection ConstantConditions
        for (HttpServer server : this.service.getServers()) {
            // noinspection ConstantConditions
            serverHostAddr = (serverConfig = server.getConfig()).getHost(true).getHostAddress();
            serverPort = serverConfig.getPort();

            Assert.assertTrue(server.hasDiscoveryTestcaseIssuerCredentialCertificatePaths(), String.format(
                "HTTP server (host=%s, port=%d) is not hosting any Discovery testcase issuer credential certificate(s).", serverHostAddr, serverPort));
            Assert.assertTrue(server.hasDiscoveryTestcaseIssuerCredentialCrlPaths(),
                String.format("HTTP server (host=%s, port=%d) is not hosting any Discovery testcase issuer credential CRL(s).", serverHostAddr, serverPort));

            // noinspection ConstantConditions
            for (String discoveryTestcaseIssuerCredCertPath : (discoveryTestcaseCredItemPaths = server.getDiscoveryTestcaseIssuerCredentialCertificatePaths())
                .keySet()) {
                Assert.assertTrue(
                    (discoveryTestcaseIssuerCredItemLookupResult =
                        lookupService.getUri((discoveryTestcaseIssuerCredItemUri =
                            new URI(ToolHttpUtils.HTTP_SCHEME, null, serverHostAddr, serverPort, discoveryTestcaseIssuerCredCertPath, null, null))))
                        .isSuccess(), String.format(
                        "Unable to GET Discovery testcase issuer credential (name=%s) certificate from HTTP server (host=%s, port=%d) hosted URI (%s): [%s]",
                        (discoveryTestcaseCredName =
                            (discoveryTestcaseCred = discoveryTestcaseCredItemPaths.get(discoveryTestcaseIssuerCredCertPath)).getName()), serverHostAddr,
                        serverPort, discoveryTestcaseIssuerCredItemUri, StringUtils.join(discoveryTestcaseIssuerCredItemLookupResult.getMessages(), "; ")));

                // noinspection ConstantConditions
                Assert.assertEquals(discoveryTestcaseIssuerCredItemLookupResult.getResponseContent(), discoveryTestcaseCred.getCredentialInfo()
                    .getCertificateDescriptor().getCertificate().getEncoded(), String.format(
                    "Discovery testcase issuer credential (name=%s) certificate does not match HTTP server (host=%s, port=%d) hosted (uri=%s) content.",
                    discoveryTestcaseCredName, serverHostAddr, serverPort, discoveryTestcaseIssuerCredItemUri));
            }

            // noinspection ConstantConditions
            for (String discoveryTestcaseIssuerCredCrlPath : (discoveryTestcaseCredItemPaths = server.getDiscoveryTestcaseIssuerCredentialCrlPaths()).keySet()) {
                Assert
                    .assertTrue(
                        (discoveryTestcaseIssuerCredItemLookupResult =
                            lookupService.getUri((discoveryTestcaseIssuerCredItemUri =
                                new URI(ToolHttpUtils.HTTP_SCHEME, null, serverHostAddr, serverPort, discoveryTestcaseIssuerCredCrlPath, null, null))))
                            .isSuccess(), String.format(
                            "Unable to GET Discovery testcase issuer credential (name=%s) CRL from HTTP server (host=%s, port=%d) hosted URI (%s): [%s]",
                            discoveryTestcaseCredItemPaths.get(discoveryTestcaseIssuerCredCrlPath).getName(), serverHostAddr, serverPort,
                            discoveryTestcaseIssuerCredItemUri, StringUtils.join(discoveryTestcaseIssuerCredItemLookupResult.getMessages(), "; ")));
            }
        }
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.http" })
    @Override
    public void startService() {
        super.startService();
    }
}
