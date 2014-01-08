package gov.hhs.onc.dcdt.utils;

import javax.annotation.Nullable;

public abstract class ToolNumberUtils {
    @Nullable
    public static <T extends Number> T defaultIfNegative(@Nullable T num, @Nullable T defaultIfNeg) {
        return !isNegative(num) ? num : defaultIfNeg;
    }

    public static <T extends Number> boolean isNegative(@Nullable T num) {
        return (num != null) && (num.doubleValue() < 0);
    }

    @Nullable
    public static <T extends Number> T defaultIfPositive(@Nullable T num, @Nullable T defaultIfPos) {
        return !isPositive(num) ? num : defaultIfPos;
    }

    public static <T extends Number> boolean isPositive(@Nullable T num) {
        return (num != null) && (num.doubleValue() > 0);
    }
}
