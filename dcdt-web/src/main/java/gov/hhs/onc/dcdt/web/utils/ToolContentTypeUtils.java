package gov.hhs.onc.dcdt.web.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.http.MediaType;

public abstract class ToolContentTypeUtils {
    public static class CompatibleContentTypePredicate implements Predicate<MediaType> {
        private Iterable<MediaType> contentTypes;

        public CompatibleContentTypePredicate(Iterable<MediaType> contentTypes) {
            this.contentTypes = contentTypes;
        }

        @Override
        public boolean evaluate(MediaType contentTypeEval) {
            for (MediaType contentType : this.contentTypes) {
                if (contentTypeEval.isCompatibleWith(contentType)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static class IncludesContentTypePredicate implements Predicate<MediaType> {
        private Iterable<MediaType> contentTypes;

        public IncludesContentTypePredicate(Iterable<MediaType> contentTypes) {
            this.contentTypes = contentTypes;
        }

        @Override
        public boolean evaluate(MediaType contentTypeEval) {
            for (MediaType contentType : this.contentTypes) {
                if (contentType.includes(contentTypeEval)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isCompatibleWith(Iterable<MediaType> contentTypes, MediaType ... contentTypesEval) {
        return isCompatibleWith(contentTypes, ToolArrayUtils.asList(contentTypesEval));
    }

    public static boolean isCompatibleWith(Iterable<MediaType> contentTypes, Iterable<MediaType> contentTypesEval) {
        return CollectionUtils.exists(contentTypesEval, new CompatibleContentTypePredicate(contentTypes));
    }

    public static MediaType findCompatibleWith(Iterable<MediaType> contentTypes, MediaType ... contentTypesEval) {
        return findCompatibleWith(contentTypes, ToolArrayUtils.asList(contentTypesEval));
    }

    public static MediaType findCompatibleWith(Iterable<MediaType> contentTypes, Iterable<MediaType> contentTypesEval) {
        return CollectionUtils.find(contentTypesEval, new CompatibleContentTypePredicate(contentTypes));
    }

    public static Collection<MediaType> selectCompatibleWith(Iterable<MediaType> contentTypes, MediaType ... contentTypesEval) {
        return selectCompatibleWith(contentTypes, ToolArrayUtils.asList(contentTypesEval));
    }

    public static Collection<MediaType> selectCompatibleWith(Iterable<MediaType> contentTypes, Iterable<MediaType> contentTypesEval) {
        return CollectionUtils.select(contentTypesEval, new CompatibleContentTypePredicate(contentTypes));
    }

    public static MediaType findIncludes(Iterable<MediaType> contentTypes, MediaType ... contentTypesEval) {
        return findIncludes(contentTypes, ToolArrayUtils.asList(contentTypesEval));
    }

    public static MediaType findIncludes(Iterable<MediaType> contentTypes, Iterable<MediaType> contentTypesEval) {
        return CollectionUtils.find(contentTypesEval, new IncludesContentTypePredicate(contentTypes));
    }
}
