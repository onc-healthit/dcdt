package gov.hhs.onc.dcdt.utils;


import java.io.File;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolFileUtils {
    public static boolean exists(String path) {
        return !StringUtils.isBlank(path) && exists(new File(path));
    }

    public static boolean exists(File file) {
        return (file != null) && file.exists();
    }

    public static boolean isDirectory(String path) {
        return !StringUtils.isBlank(path) && isDirectory(new File(path));
    }

    public static boolean isDirectory(File file) {
        return (file != null) && file.isDirectory();
    }

    public static boolean isFile(String path) {
        return !StringUtils.isBlank(path) && isFile(new File(path));
    }

    public static boolean isFile(File file) {
        return (file != null) && file.isFile();
    }
}
