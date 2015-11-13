package gov.hhs.onc.dcdt.http.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.dns.lookup.DnsNameService;
import io.netty.handler.codec.http.HttpMethod;
import java.net.URI;
import javax.annotation.Nonnegative;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public interface HttpLookupService extends ToolBean {
    public HttpLookupResult getUri(URI reqUri);

    public HttpLookupResult lookupUri(URI reqUri, HttpMethod reqMethod);

    @Nonnegative
    public int getConnectTimeout();

    public void setConnectTimeout(@Nonnegative int connTimeout);

    public DnsNameService getDnsNameService();

    public void setDnsNameService(DnsNameService dnsNameService);

    @Nonnegative
    public int getMaxContentLength();

    public void setMaxContentLength(@Nonnegative int maxContentLen);

    @Nonnegative
    public int getReadTimeout();

    public void setReadTimeout(@Nonnegative int readTimeout);

    public ThreadPoolTaskExecutor getTaskExecutor();

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExec);
}
