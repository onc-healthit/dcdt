package gov.hhs.onc.dcdt.net.mime.utils;

import gov.hhs.onc.dcdt.net.mime.CoreContentTypes;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.Comparator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.util.MimeType;

public abstract class ToolMimeTypeUtils {
    public final static String DELIM_TYPE = "/";
    public final static String DELIM_PARAM = "; ";
    public final static String DELIM_PARAM_VALUE = "=";

    public final static Comparator<MimeType> MIME_TYPE_COMPARATOR_INSTANCE = (mimeType1, mimeType2) -> ToolMimeTypeUtils.compareTo(true, mimeType1, mimeType2);
    public final static Comparator<MimeType> MIME_TYPE_COMPARATOR_INSTANCE_BASE_TYPE = (mimeType1, mimeType2) -> ToolMimeTypeUtils.compareTo(false,
        mimeType1, mimeType2);

    public static Entry<String, String> transformMimeTypeParameterEntry(String paramName, String paramValue) {
        return new MutablePair<>(paramName, paramValue);
    }

    public static boolean isCompatible(MimeType mimeType, MimeType ... mimeTypeEvals) {
        return isCompatible(mimeType, ToolArrayUtils.asList(mimeTypeEvals));
    }

    public static boolean isCompatible(MimeType mimeType, Iterable<MimeType> mimeTypeEvals) {
        return ToolStreamUtils.exists(mimeTypeEvals, mimeType::isCompatibleWith);
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
        return ToolStreamUtils.exists(mimeTypeEvals, mimeTypeEval -> compareTo(includeParams, mimeType, mimeTypeEval) == 0);
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
        return new MimeType(StringUtils.defaultIfBlank(type, CoreContentTypes.WILDCARD_TYPE), StringUtils.defaultIfBlank(subtype,
            CoreContentTypes.WILDCARD_TYPE), ToolMapUtils.toMap(String.class, String.class, params));
    }
}
