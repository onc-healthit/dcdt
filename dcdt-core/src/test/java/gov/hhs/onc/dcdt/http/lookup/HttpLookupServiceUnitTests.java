package gov.hhs.onc.dcdt.http.lookup;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import java.net.URI;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.http.all", "dcdt.test.unit.http.lookup.all", "dcdt.test.unit.http.lookup.service" })
public class HttpLookupServiceUnitTests extends AbstractToolUnitTests {
    @Resource(name = "httpLookupServiceCombined")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HttpLookupService httpLookupService;

    @Value("${dcdt.test.http.lookup.1.uri}")
    private URI testHttpLookup1Uri;

    @Test
    public void testGetUri() throws Exception {
        HttpLookupResult result = this.httpLookupService.getUri(this.testHttpLookup1Uri);

        Assert.assertTrue(
            result.isSuccess(),
            String.format("HTTP GET lookup (reqUri=%s) failed (respStatus={%s}): [%s]", this.testHttpLookup1Uri, result.getResponseStatus(),
                StringUtils.join(result.getMessages(), ", ")));

        Assert.assertTrue(result.hasResponseContent(), String.format("HTTP GET lookup (reqUri=%s) has no response content.", this.testHttpLookup1Uri));
    }
}
