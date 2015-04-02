package gov.hhs.onc.dcdt.utils;

import java.util.regex.Matcher;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

public abstract class ToolRegexUtils {
    @Nullable
    public static String[] groups(@Nullable Matcher matcher) {
        return groups(matcher, false);
    }

    @Nullable
    public static String[] groups(@Nullable Matcher matcher, boolean includeEntireMatch) {
        if ((matcher == null) || !matcher.matches()) {
            return null;
        }

        int groupOffset = (includeEntireMatch ? 1 : 0), groupNum = matcher.groupCount() + groupOffset;

        return IntStream.range(0, groupNum).mapToObj(a -> matcher.group((a + 1 - groupOffset))).toArray(String[]::new);
    }
}
