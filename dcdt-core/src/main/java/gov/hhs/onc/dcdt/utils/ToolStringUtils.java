package gov.hhs.onc.dcdt.utils;

import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

public abstract class ToolStringUtils {
    @SuppressWarnings({ "serial" })
    public static class ToolStrBuilder extends StrBuilder {
        public ToolStrBuilder() {
            super();
        }

        public ToolStrBuilder(int initialCapacity) {
            super(initialCapacity);
        }

        public ToolStrBuilder(String str) {
            super(str);
        }

        public <T> StrBuilder appendWithDelimiters(@Nullable T[] objs, @Nullable String delim) {
            return this.appendWithDelimiters(ToolArrayUtils.asList(objs), delim);
        }

        public <T> StrBuilder appendWithDelimiters(@Nullable Iterable<T> objs, @Nullable String delim) {
            if (objs != null) {
                for (T obj : objs) {
                    this.appendWithDelimiter(obj, delim);
                }
            }

            return this;
        }

        public StrBuilder appendWithDelimiter(@Nullable Object obj, @Nullable String delim) {
            String objStr = StringUtils.removeStart(Objects.toString(obj, this.getNullText()), delim);

            return (objStr != null) ? this.appendDelimiter(delim).append(objStr) : this;
        }

        public StrBuilder appendDelimiter(@Nullable String delim) {
            return !this.endsWith(delim) ? this.appendSeparator(delim) : this;
        }
    }

    public final static String QUOTE_SINGLE = "'";
    public final static String QUOTE_DBL = "\"";

    public final static String DELIM_CHARS_NEWLINE = StringUtils.CR + StringUtils.LF;

    @Nullable
    public static String[] splitLines(@Nullable String str) {
        return splitLines(str, -1);
    }

    @Nullable
    public static String[] splitLines(@Nullable String str, int limit) {
        return StringUtils.split(str, DELIM_CHARS_NEWLINE, limit);
    }

    @Nullable
    public static <T> String joinDelimit(@Nullable T[] objs, @Nullable String delim) {
        return joinDelimit(ToolArrayUtils.asList(objs), delim);
    }

    @Nullable
    public static <T> String joinDelimit(@Nullable Iterable<T> objs) {
        return joinDelimit(objs, null);
    }

    @Nullable
    public static <T> String joinDelimit(@Nullable Iterable<T> objs, @Nullable String delim) {
        return (objs != null) ? new ToolStrBuilder().appendWithDelimiters(objs, delim).toString() : null;
    }

    public static String quote(String str) {
        return quote(str, QUOTE_DBL);
    }

    public static String quote(String str, String quoteStr) {
        return StringUtils.appendIfMissing(StringUtils.prependIfMissing(str, quoteStr), quoteStr);
    }
}
