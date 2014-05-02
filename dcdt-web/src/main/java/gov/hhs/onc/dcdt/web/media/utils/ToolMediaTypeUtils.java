package gov.hhs.onc.dcdt.web.media.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

public abstract class ToolMediaTypeUtils {
    public static class IncludesMediaTypePredicate extends AbstractToolPredicate<MediaType> {
        private Iterable<MediaType> mediaTypes;

        public IncludesMediaTypePredicate(Iterable<MediaType> mediaTypes) {
            this.mediaTypes = mediaTypes;
        }

        @Override
        protected boolean evaluateInternal(MediaType mediaTypeEval) throws Exception {
            for (MediaType mediaType : this.mediaTypes) {
                if (mediaType.includes(mediaTypeEval)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isIncluded(Iterable<MediaType> mediaTypes, MediaType ... mediaTypesEval) {
        return isIncluded(mediaTypes, ToolArrayUtils.asList(mediaTypesEval));
    }

    public static boolean isIncluded(Iterable<MediaType> mediaTypes, Iterable<MediaType> mediaTypesEval) {
        return CollectionUtils.exists(mediaTypesEval, new IncludesMediaTypePredicate(mediaTypes));
    }

    @Nullable
    public static MediaType findIncluded(Iterable<MediaType> mediaTypes, MediaType ... mediaTypesEval) {
        return findIncluded(mediaTypes, ToolArrayUtils.asList(mediaTypesEval));
    }

    @Nullable
    public static MediaType findIncluded(Iterable<MediaType> mediaTypes, Iterable<MediaType> mediaTypesEval) {
        return CollectionUtils.find(mediaTypesEval, new IncludesMediaTypePredicate(mediaTypes));
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
