package gov.hhs.onc.dcdt.net.mime.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import java.util.Comparator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.util.MimeType;

public abstract class ToolMimeTypeUtils {
    public static class MimeTypeParameterEntryTransformer extends AbstractToolTransformer<String, Entry<String, String>> {
        private String paramName;

        public MimeTypeParameterEntryTransformer(String paramName) {
            this.paramName = paramName;
        }

        @Override
        protected Entry<String, String> transformInternal(String paramValue) throws Exception {
            return new MutablePair<>(this.paramName, paramValue);
        }
    }

    public static class MimeTypeComparator implements Comparator<MimeType> {
        public final static MimeTypeComparator INSTANCE = new MimeTypeComparator(true);
        public final static MimeTypeComparator INSTANCE_BASE_TYPE = new MimeTypeComparator(false);

        private boolean includeParams;

        public MimeTypeComparator(boolean includeParams) {
            this.includeParams = includeParams;
        }

        @Override
        public int compare(MimeType mimeType1, MimeType mimeType2) {
            return compareTo(this.includeParams, mimeType1, mimeType2);
        }
    }

    public static class MimeTypeCompatabilityPredicate extends AbstractMimeTypePredicate {
        public MimeTypeCompatabilityPredicate(MimeType mimeType) {
            super(mimeType);
        }

        @Override
        protected boolean evaluateInternal(MimeType mimeTypeEval) throws Exception {
            return this.mimeType.isCompatibleWith(mimeTypeEval);
        }
    }

    public static class MimeTypeEqualsPredicate extends AbstractMimeTypePredicate {
        private boolean includeParams;

        public MimeTypeEqualsPredicate(boolean includeParams, MimeType mimeType) {
            super(mimeType);

            this.includeParams = includeParams;
        }

        @Override
        protected boolean evaluateInternal(MimeType mimeTypeEval) throws Exception {
            return (compareTo(this.includeParams, this.mimeType, mimeTypeEval) == 0);
        }
    }

    private abstract static class AbstractMimeTypePredicate extends AbstractToolPredicate<MimeType> {
        protected MimeType mimeType;

        protected AbstractMimeTypePredicate(MimeType mimeType) {
            this.mimeType = mimeType;
        }

        @Override
        protected abstract boolean evaluateInternal(MimeType mimeTypeEval) throws Exception;
    }

    public final static String DELIM_TYPE = "/";
    public final static String DELIM_PARAM = "; ";
    public final static String DELIM_PARAM_VALUE = "=";

    public final static String TYPE_WILDCARD = "*";

    public final static String TYPE_APP = "application";

    public static boolean isCompatible(MimeType mimeType, MimeType ... mimeTypeEvals) {
        return isCompatible(mimeType, ToolArrayUtils.asList(mimeTypeEvals));
    }

    public static boolean isCompatible(MimeType mimeType, Iterable<MimeType> mimeTypeEvals) {
        return CollectionUtils.exists(mimeTypeEvals, new MimeTypeCompatabilityPredicate(mimeType));
    }

    public static boolean equals(MimeType mimeType, MimeType ... mimeTypeEvals) {
        return equals(mimeType, ToolArrayUtils.asList(mimeTypeEvals));
    }

    public static boolean equals(MimeType mimeType, Iterable<MimeType> mimeTypeEvals) {
        return equals(true, mimeType, mimeTypeEvals);
    }

    public static boolean equals(boolean includeParams, MimeType mimeType, MimeType ... mimeTypeEvals) {
        return equals(includeParams, mimeType, ToolArrayUtils.asList(mimeTypeEvals));
    }

    public static boolean equals(boolean includeParams, MimeType mimeType, Iterable<MimeType> mimeTypeEvals) {
        return CollectionUtils.exists(mimeTypeEvals, new MimeTypeEqualsPredicate(includeParams, mimeType));
    }

    public static int compareTo(MimeType mimeType1, MimeType mimeType2) {
        return compareTo(true, mimeType1, mimeType2);
    }

    public static int compareTo(boolean includeParams, MimeType mimeType1, MimeType mimeType2) {
        return (includeParams ? forParameters(mimeType1, mimeType2) : forBaseType(mimeType1)).compareTo((includeParams ? mimeType2 : forBaseType(mimeType2)));
    }

    public static MimeType forParameters(MimeType mimeType1, MimeType mimeType2) {
        return new MimeType(mimeType1, mimeType2.getParameters());
    }

    public static MimeType forBaseType(MimeType mimeType) {
        return (hasParameters(mimeType) ? new MimeType(mimeType, null) : mimeType);
    }

    public static String getBaseType(MimeType mimeType) {
        return (mimeType.getType() + DELIM_TYPE + mimeType.getSubtype());
    }

    public static boolean hasParameter(MimeType mimeType, String paramName) {
        return (hasParameters(mimeType) && (mimeType.getParameters().containsKey(paramName)));
    }

    public static boolean hasParameters(MimeType mimeType) {
        return !mimeType.getParameters().isEmpty();
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static MimeType valueOf(String type, @Nullable Entry<String, String> ... params) {
        return valueOf(type, ToolArrayUtils.asList(params));
    }

    public static MimeType valueOf(String type, @Nullable Iterable<Entry<String, String>> params) {
        return valueOf(type, null, params);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static MimeType valueOf(@Nullable String type, @Nullable String subtype, @Nullable Entry<String, String> ... params) {
        return valueOf(type, subtype, ToolArrayUtils.asList(params));
    }

    public static MimeType valueOf(@Nullable String type, @Nullable String subtype, @Nullable Iterable<Entry<String, String>> params) {
        return new MimeType(StringUtils.defaultIfBlank(type, TYPE_WILDCARD), StringUtils.defaultIfBlank(subtype, TYPE_WILDCARD), ToolMapUtils.toMap(
            String.class, String.class, params));
    }
}
