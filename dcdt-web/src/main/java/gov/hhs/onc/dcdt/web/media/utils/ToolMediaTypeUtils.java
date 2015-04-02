package gov.hhs.onc.dcdt.web.media.utils;

import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

public abstract class ToolMediaTypeUtils {
    public static boolean isIncluded(Iterable<MediaType> mediaTypes, MediaType ... mediaTypesEval) {
        return isIncluded(mediaTypes, ToolArrayUtils.asList(mediaTypesEval));
    }

    public static boolean isIncluded(Iterable<MediaType> mediaTypes, Iterable<MediaType> mediaTypesEval) {
        return ToolStreamUtils.exists(mediaTypesEval, mediaTypeEval -> isIncluded(mediaTypeEval, mediaTypes));
    }

    public static boolean isIncluded(MediaType mediaTypeEval, Iterable<MediaType> mediaTypes) {
        return ToolStreamUtils.exists(mediaTypes, mediaType -> mediaType.includes(mediaTypeEval));
    }

    @Nullable
    public static MediaType findIncluded(Iterable<MediaType> mediaTypes, MediaType ... mediaTypesEval) {
        return findIncluded(mediaTypes, ToolArrayUtils.asList(mediaTypesEval));
    }

    @Nullable
    public static MediaType findIncluded(Iterable<MediaType> mediaTypes, Iterable<MediaType> mediaTypesEval) {
        return ToolStreamUtils.find(mediaTypesEval, mediaTypeEval -> isIncluded(mediaTypeEval, mediaTypes));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static MediaType valueOf(String type, @Nullable Entry<String, String> ... params) {
        return valueOf(type, ToolArrayUtils.asList(params));
    }

    public static MediaType valueOf(String type, @Nullable Iterable<Entry<String, String>> params) {
        return valueOf(type, null, params);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static MediaType valueOf(@Nullable String type, @Nullable String subtype, @Nullable Entry<String, String> ... params) {
        return valueOf(type, subtype, ToolArrayUtils.asList(params));
    }

    public static MediaType valueOf(@Nullable String type, @Nullable String subtype, @Nullable Iterable<Entry<String, String>> params) {
        return valueOf(ToolMimeTypeUtils.valueOf(type, subtype, params));
    }

    public static MediaType valueOf(MimeType mimeType) {
        return new MediaType(mimeType.getType(), mimeType.getSubtype(), mimeType.getParameters());
    }
}
