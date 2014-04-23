package gov.hhs.onc.dcdt.web.media;

import gov.hhs.onc.dcdt.net.mime.CoreContentTypes;
import gov.hhs.onc.dcdt.web.media.utils.ToolMediaTypeUtils;
import org.springframework.http.MediaType;

public final class WebContentTypes {
    public final static String JAVASCRIPT_SUBTYPE = "javascript";

    public final static MediaType APP_JAVASCRIPT = ToolMediaTypeUtils.valueOf(CoreContentTypes.APP_TYPE, JAVASCRIPT_SUBTYPE);

    public final static String TEXT_CSS_SUBTYPE = "css";
    public final static MediaType TEXT_CSS = ToolMediaTypeUtils.valueOf(CoreContentTypes.TEXT_TYPE, TEXT_CSS_SUBTYPE);

    public final static MediaType TEXT_JAVASCRIPT = ToolMediaTypeUtils.valueOf(CoreContentTypes.TEXT_TYPE, JAVASCRIPT_SUBTYPE);

    private WebContentTypes() {
    }
}
