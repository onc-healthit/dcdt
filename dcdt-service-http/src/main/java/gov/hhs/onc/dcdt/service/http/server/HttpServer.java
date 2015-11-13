package gov.hhs.onc.dcdt.service.http.server;

import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolChannelServer;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import java.util.Map;
import javax.annotation.Nullable;

public interface HttpServer extends ToolChannelServer<HttpServerConfig> {
    public boolean hasDiscoveryTestcaseIssuerCredentialCertificatePaths();

    @Nullable
    public Map<String, DiscoveryTestcaseCredential> getDiscoveryTestcaseIssuerCredentialCertificatePaths();

    public boolean hasDiscoveryTestcaseIssuerCredentialCrlPaths();

    @Nullable
    public Map<String, DiscoveryTestcaseCredential> getDiscoveryTestcaseIssuerCredentialCrlPaths();
}
