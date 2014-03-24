package gov.hhs.onc.dcdt.web;

/**
 * @see org.springframework.http.HttpHeaders
 */
public interface ToolHttpHeaders {
    public final static char SUFFIX_EQUALS = '=';

    public final static String ACCEPT = "Accept";

    public final static String ACCEPT_EQUALS = ACCEPT + SUFFIX_EQUALS;

    public final static String ACCEPT_CHARSET = "Accept-Charset";

    public final static String ACCEPT_CHARSET_EQUALS = ACCEPT_CHARSET + SUFFIX_EQUALS;

    public final static String ALLOW = "Allow";

    public final static String ALLOW_EQUALS = ALLOW + SUFFIX_EQUALS;

    public final static String CACHE_CONTROL = "Cache-Control";

    public final static String CACHE_CONTROL_EQUALS = CACHE_CONTROL + SUFFIX_EQUALS;

    public final static String CONTENT_DISPOSITION = "Content-Disposition";

    public final static String CONTENT_DISPOSITION_EQUALS = CONTENT_DISPOSITION + SUFFIX_EQUALS;

    public final static String CONTENT_LENGTH = "Content-Length";

    public final static String CONTENT_LENGTH_EQUALS = CONTENT_LENGTH + SUFFIX_EQUALS;

    public final static String CONTENT_TYPE = "Content-Type";

    public final static String CONTENT_TYPE_EQUALS = CONTENT_TYPE + SUFFIX_EQUALS;

    public final static String DATE = "Date";

    public final static String DATE_EQUALS = DATE + SUFFIX_EQUALS;

    public final static String ETAG = "ETag";

    public final static String ETAG_EQUALS = ETAG + SUFFIX_EQUALS;

    public final static String EXPIRES = "Expires";

    public final static String EXPIRES_EQUALS = EXPIRES + SUFFIX_EQUALS;

    public final static String IF_MODIFIED_SINCE = "If-Modified-Since";

    public final static String IF_MODIFIED_SINCE_EQUALS = IF_MODIFIED_SINCE + SUFFIX_EQUALS;

    public final static String IF_NONE_MATCH = "If-None-Match";

    public final static String IF_NONE_MATCH_EQUALS = IF_NONE_MATCH + SUFFIX_EQUALS;

    public final static String LAST_MODIFIED = "Last-Modified";

    public final static String LAST_MODIFIED_EQUALS = LAST_MODIFIED + SUFFIX_EQUALS;

    public final static String LOCATION = "Location";

    public final static String LOCATION_EQUALS = LOCATION + SUFFIX_EQUALS;

    public final static String PRAGMA = "Pragma";
}
