package gov.hhs.onc.dcdt.utils;

import java.util.regex.Matcher;
import javax.annotation.Nullable;

public abstract class ToolRegexUtils {
    @Nullable
    public static String[] groups(Matcher matcher) {
        return groups(matcher, false);
    }

    @Nullable
    public static String[] groups(Matcher matcher, boolean includeEntireMatch) {
        if (!matcher.matches()) {
            return null;
        }

        int groupOffset = (includeEntireMatch ? 1 : 0), groupNum = matcher.groupCount() + groupOffset;
        String[] groups = new String[groupNum];

        for (int a = 0; a < groupNum; a++) {
            groups[a] = matcher.group((a + 1 - groupOffset));
        }

        return groups;
    }
}
