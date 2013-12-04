package gov.hhs.onc.dcdt.utils;

public abstract class ToolReflectionUtils {
    public static boolean hasModifiers(int mods1, int mods2) {
        return (mods1 & mods2) != 0;
    }
}
