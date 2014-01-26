package gov.hhs.onc.dcdt.utils;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

public abstract class ToolStringUtils {
    public final static String QUOTE_SINGLE = "'";
    public final static String QUOTE_DBL = "\"";

    public static <T> String joinDelimit(T[] items) {
        return joinDelimit(items, null);
    }

    public static <T> String joinDelimit(T[] items, String delim) {
        return joinDelimit(ToolArrayUtils.asList(items), delim);
    }

    public static <T> String joinDelimit(Iterable<T> items) {
        return joinDelimit(items, null);
    }

    public static <T> String joinDelimit(Iterable<T> items, String delim) {
        if (delim == null) {
            return StringUtils.join(items, delim);
        }

        StrBuilder strBuilder = new StrBuilder();

        for (T item : items) {
            strBuilder.appendSeparator(delim);
            strBuilder.append(StringUtils.stripEnd(Objects.toString(item), delim));
        }

        return strBuilder.toString();
    }

    public static String quote(String str) {
        return quote(str, QUOTE_DBL);
    }

    public static String quote(String str, String quoteStr) {
        return StringUtils.appendIfMissing(StringUtils.prependIfMissing(str, quoteStr), quoteStr);
    }
}
