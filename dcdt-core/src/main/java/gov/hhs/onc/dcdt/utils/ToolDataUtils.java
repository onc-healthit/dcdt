package gov.hhs.onc.dcdt.utils;

public final class ToolDataUtils {
    public final static long MB_IN_GB = 1024L;

    public final static long KB_IN_MB = 1024L;
    public final static long KB_IN_GB = KB_IN_MB * MB_IN_GB;

    public final static long BYTES_IN_KB = 1024L;
    public final static long BYTES_IN_MB = KB_IN_MB * BYTES_IN_KB;
    public final static long BYTES_IN_GB = KB_IN_GB * BYTES_IN_KB;

    private ToolDataUtils() {
    }
}
