package gov.hhs.onc.dcdt.http.lookup.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLookupResultBean;
import gov.hhs.onc.dcdt.http.lookup.HttpLookupResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class HttpLookupResultImpl extends AbstractToolLookupResultBean implements HttpLookupResult {
    private URI reqUri;
    private HttpMethod reqMethod;
    private List<ToolMessage> msgs = new ArrayList<>();
    private InetSocketAddress remoteSocketAddr;
    private HttpHeaders reqHeaders;
    private byte[] respContent;
    private HttpHeaders respHeaders;
    private HttpResponseStatus respStatus;

    public HttpLookupResultImpl(URI reqUri, HttpMethod reqMethod) {
        this.reqUri = reqUri;
        this.reqMethod = reqMethod;
    }

    @Override
    public List<ToolMessage> getMessages() {
        return this.msgs;
    }

    @Override
    public boolean hasRemoteSocketAddress() {
        return (this.remoteSocketAddr != null);
    }

    @Nullable
    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        return this.remoteSocketAddr;
    }

    @Override
    public void setRemoteSocketAddress(InetSocketAddress remoteSocketAddr) {
        this.remoteSocketAddr = remoteSocketAddr;
    }

    @Override
    public boolean hasRequestHeaders() {
        return (this.reqHeaders != null);
    }

    @Nullable
    @Override
    public HttpHeaders getRequestHeaders() {
        return this.reqHeaders;
    }

    @Override
    public void setRequestHeaders(HttpHeaders reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    @Override
    public HttpMethod getRequestMethod() {
        return this.reqMethod;
    }

    @Override
    public URI getRequestUri() {
        return this.reqUri;
    }

    @Override
    public boolean hasResponseContent() {
        return (this.respContent != null);
    }

    @Nullable
    @Override
    public byte[] getResponseContent() {
        return this.respContent;
    }

    @Override
    public void setResponseContent(byte ... respContent) {
        this.respContent = respContent;
    }

    @Override
    public boolean hasResponseHeaders() {
        return (this.respHeaders != null);
    }

    @Nullable
    @Override
    public HttpHeaders getResponseHeaders() {
        return this.respHeaders;
    }

    @Override
    public void setResponseHeaders(HttpHeaders respHeaders) {
        this.respHeaders = respHeaders;
    }

    @Override
    public boolean hasResponseStatus() {
        return (this.respStatus != null);
    }

    @Nullable
    @Override
    public HttpResponseStatus getResponseStatus() {
        return this.respStatus;
    }

    @Override
    public void setResponseStatus(HttpResponseStatus respStatus) {
        this.respStatus = respStatus;
    }

    @Override
    public boolean isSuccess() {
        return (super.isSuccess() && this.hasResponseStatus() && (this.respStatus.code() == HttpResponseStatus.OK.code()));
    }
}
