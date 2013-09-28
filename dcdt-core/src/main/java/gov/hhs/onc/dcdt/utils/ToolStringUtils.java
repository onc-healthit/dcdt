package gov.hhs.onc.dcdt.utils;


import java.util.Arrays;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

public abstract class ToolStringUtils {
    public static <T> String joinDelimit(T[] items) {
        return joinDelimit(items, null);
    }

    public static <T> String joinDelimit(T[] items, String delim) {
        return joinDelimit(Arrays.asList(items), delim);
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
            strBuilder.append(StringUtils.stripEnd(ObjectUtils.toString(item), delim));
        }

        return strBuilder.toString();
    }
}
