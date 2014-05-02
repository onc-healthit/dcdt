package gov.hhs.onc.dcdt.web.filter;

import java.util.Set;
import javax.servlet.Filter;
import org.springframework.http.MediaType;

public interface ContentFilter<T extends Filter> extends Filter {
    public Set<MediaType> getContentTypes();

    public void setContentTypes(Set<MediaType> contentTypes);
}
