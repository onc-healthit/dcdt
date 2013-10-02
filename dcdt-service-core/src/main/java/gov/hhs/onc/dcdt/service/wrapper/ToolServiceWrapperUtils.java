package gov.hhs.onc.dcdt.service.wrapper;


import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.tanukisoftware.wrapper.WrapperManager;

public abstract class ToolServiceWrapperUtils {
    public final static String WRAPPER_PROP_NAME_DELIM = ".";
    public final static String WRAPPER_PROP_NAME_PREFIX = "wrapper";
    public final static String WRAPPER_NAME_DISPLAY_PROP_NAME = buildPropertyName("displayname");
    public final static String WRAPPER_NAME_PROP_NAME = buildPropertyName("name");
    public final static String WRAPPER_PID_FILE_PROP_NAME = buildPropertyName("pidfile");
    public final static String WRAPPER_STATUS_FILE_PROP_NAME = buildPropertyName("statusfile");

    public static String getWrapperDisplayName() {
        return getWrapperProperty(WRAPPER_NAME_DISPLAY_PROP_NAME);
    }

    public static String getWrapperName() {
        return getWrapperProperty(WRAPPER_NAME_PROP_NAME);
    }

    public static ToolServiceWrapperStatus getWrapperStatus() {
        return new ToolServiceWrapperStatus(readWrapperStatus(), readWrapperPid());
    }

    public static long readWrapperPid() {
        String wrapperPidStr = readWrapperFile(WRAPPER_PID_FILE_PROP_NAME);

        if (NumberUtils.isDigits(wrapperPidStr)) {
            long wrapperPid = Long.parseLong(wrapperPidStr);

            if (wrapperPid > 0) {
                return wrapperPid;
            }
        }

        return -1;
    }

    public static ToolServiceWrapperStatusName readWrapperStatus() {
        String wrapperStatusStr = readWrapperFile(WRAPPER_STATUS_FILE_PROP_NAME);

        if (!StringUtils.isBlank(wrapperStatusStr)) {
            for (ToolServiceWrapperStatusName wrapperStatus : EnumSet.allOf(ToolServiceWrapperStatusName.class)) {
                if (wrapperStatus.getName().equalsIgnoreCase(wrapperStatusStr)) {
                    return wrapperStatus;
                }
            }
        }

        return ToolServiceWrapperStatusName.STOPPED;
    }

    public static String readWrapperFile(String wrapperPropName) {
        if (hasWrapperProperty(wrapperPropName)) {
            File wrapperFile = new File(getWrapperProperty(wrapperPropName));

            if (wrapperFile.exists() && wrapperFile.isFile() && wrapperFile.canRead()) {
                try {
                    return FileUtils.readFileToString(wrapperFile).trim();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static String getWrapperProperty(String wrapperPropName) {
        return hasWrapperProperty(wrapperPropName) ? StringUtils.trim(WrapperManager.getProperties().getProperty(wrapperPropName)) : null;
    }

    public static boolean hasWrapperProperty(String wrapperPropName) {
        return WrapperManager.getProperties().containsKey(wrapperPropName);
    }

    private static String buildPropertyName(String ... wrapperPropNameParts) {
        wrapperPropNameParts = ArrayUtils.add(wrapperPropNameParts, 0, WRAPPER_PROP_NAME_PREFIX);

        return StringUtils.join(wrapperPropNameParts, WRAPPER_PROP_NAME_DELIM);
    }
}
