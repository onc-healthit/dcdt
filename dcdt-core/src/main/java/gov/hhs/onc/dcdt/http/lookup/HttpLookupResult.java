package gov.hhs.onc.dcdt.http.lookup;

import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.net.InetSocketAddress;
import java.net.URI;
import javax.annotation.Nullable;

public interface HttpLookupResult extends ToolLookupResultBean {
    public boolean hasRemoteSocketAddress();

    @Nullable
    public InetSocketAddress getRemoteSocketAddress();

    public void setRemoteSocketAddress(InetSocketAddress remoteSocketAddr);

    public boolean hasRequestHeaders();

    @Nullable
    public HttpHeaders getRequestHeaders();

    public void setRequestHeaders(HttpHeaders reqHeaders);

    public HttpMethod getRequestMethod();

    public URI getRequestUri();

    public boolean hasResponseContent();

    @Nullable
    public byte[] getResponseContent();

    public void setResponseContent(byte ... respContent);

    public boolean hasResponseHeaders();

    @Nullable
    public HttpHeaders getResponseHeaders();

    public void setResponseHeaders(HttpHeaders respHeaders);

    public boolean hasResponseStatus();

    @Nullable
    public HttpResponseStatus getResponseStatus();

    public void setResponseStatus(HttpResponseStatus respStatus);
}
