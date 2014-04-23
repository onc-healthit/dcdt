package gov.hhs.onc.dcdt.web.filter.impl;


import gov.hhs.onc.dcdt.web.filter.WebContentFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.filter.OncePerRequestFilter;

public class ToolCharacterEncodingFilter extends OncePerRequestFilter implements WebContentFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolCharacterEncodingFilter.class);

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ContentNegotiationManager contentNegotiationManager;

    private Charset enc;
    private Set<MediaType> contentTypes = new TreeSet<>(MediaType.SPECIFICITY_COMPARATOR);

    @Override
    protected void doFilterInternal(HttpServletRequest servletReq, HttpServletResponse servletResp, FilterChain filterChain) throws ServletException,
        IOException {

        // @formatter:off
        /*
        String servletRespContentTypeStr = servletResp.getContentType();
        MediaType servletRespContentType;

        if (((servletRespContentType =
            ToolMediaTypeUtils.findIncluded(
                this.contentTypes,
                ((servletRespContentTypeStr != null)
                    ? ToolArrayUtils.asSet(MediaType.parseMediaType(servletRespContentTypeStr)) : this.contentNegotiationManager
                        .resolveMediaTypes(new ServletWebRequest(servletReq))))) != null)
            && servletRespContentType.isConcrete()) {
            super.doFilter(servletReq, servletResp, filterChain);

            LOGGER.trace(String.format("Servlet request (uri=%s) content (contentType={%s}) filtered using character encoding (enc=%s).",
                servletReq.getRequestURI(), servletRespContentType, this.enc.name()));
        } else {
            filterChain.doFilter(servletReq, servletResp);
        }
        */
        // @formatter:on
    }

    @Override
    public Set<MediaType> getContentTypes() {
        return this.contentTypes;
    }

    @Override
    public void setContentTypes(Set<MediaType> contentTypes) {
        this.contentTypes.clear();
        this.contentTypes.addAll(contentTypes);
    }

    public Charset getEncoding() {
        return enc;
    }

    public void setEncoding(Charset enc) {
        this.enc = enc;
    }
}
