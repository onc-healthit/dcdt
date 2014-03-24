package gov.hhs.onc.dcdt.utils;

import javax.annotation.Nullable;

public abstract class ToolNumberUtils {
    public static <T extends Number> boolean isNegative(@Nullable T num) {
        return (num != null) && (num.doubleValue() < 0);
    }

    public static <T extends Number> boolean isPositive(@Nullable T num) {
        return (num != null) && (num.doubleValue() > 0);
    }
}
