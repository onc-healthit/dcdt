package gov.hhs.onc.dcdt.utils;

public abstract class ToolNumberUtils {
    public static <T extends Number> T defaultIfNegative(T num, T defaultIfNeg) {
        return !isNegative(num) ? num : defaultIfNeg;
    }

    public static <T extends Number> boolean isNegative(T num) {
        return num.doubleValue() < 0;
    }

    public static <T extends Number> T defaultIfPositive(T num, T defaultIfPos) {
        return !isPositive(num) ? num : defaultIfPos;
    }

    public static <T extends Number> boolean isPositive(T num) {
        return num.doubleValue() > 0;
    }
}
