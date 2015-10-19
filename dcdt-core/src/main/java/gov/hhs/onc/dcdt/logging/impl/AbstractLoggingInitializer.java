package gov.hhs.onc.dcdt.logging.impl;

import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.logging.LoggingInitializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ResourceUtils;

public abstract class AbstractLoggingInitializer implements LoggingInitializer {
    protected final static String CONFIG_FILE_URL_PATH_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX + "logback/logback-";

    protected final static String CONFIG_FILE_URL_PATH_SUFFIX = FilenameUtils.EXTENSION_SEPARATOR + "groovy";

    protected String appName;
    protected String appSuffix;

    protected AbstractLoggingInitializer(String appName, String appSuffix) {
        this.appName = appName;
        this.appSuffix = appSuffix;
    }

    @Override
    public String buildLogFileName() {
        String logFileName = System.getProperty(ToolProperties.LOG_FILE_NAME_NAME, this.appName);

        if (StringUtils.isBlank(logFileName)) {
            throw new ApplicationContextException("Unable to determine log file name.");
        }

        return logFileName;
    }

    @Override
    public File buildLogDirectory() {
        String logDirPath = this.buildLogDirectoryPath();

        if (StringUtils.isBlank(logDirPath)) {
            throw new ApplicationContextException("Unable to determine log file directory path.");
        }

        File logDir = new File(logDirPath);

        logDirPath = logDir.getPath();

        if (!logDir.exists()) {
            if (!logDir.mkdirs()) {
                throw new ApplicationContextException(String.format("Unable to create log file directory (path=%s).", logDirPath));
            }
        } else if (!logDir.isDirectory()) {
            throw new ApplicationContextException(String.format("Log file directory path (%s) is not a directory.", logDirPath));
        }

        return logDir;
    }

    @Override
    public String buildLogContextName() {
        return System.getProperty(ToolProperties.LOG_CONTEXT_NAME_NAME, this.appName);
    }

    @Override
    public URL buildLogConfigFileUrl() {
        String logConfigFileUrlPath = CONFIG_FILE_URL_PATH_PREFIX + this.appSuffix + CONFIG_FILE_URL_PATH_SUFFIX;

        try {
            return ResourceUtils.getURL(logConfigFileUrlPath);
        } catch (FileNotFoundException e) {
            throw new ApplicationContextException(String.format("Unable to determine Logback configuration file URL (path=%s).", logConfigFileUrlPath), e);
        }
    }

    @Nullable
    protected String buildLogDirectoryPath() {
        return System.getProperty(ToolProperties.LOG_DIR_NAME);
    }
}
