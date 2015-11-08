package gov.hhs.onc.dcdt.logging.impl;

import gov.hhs.onc.dcdt.context.ToolApplicationContextException;
import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.context.impl.AbstractToolInitializer;
import gov.hhs.onc.dcdt.logging.LoggingInitializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.ResourceUtils;

public abstract class AbstractLoggingInitializer extends AbstractToolInitializer implements LoggingInitializer {
    protected final static String CONFIG_FILE_URL_PATH_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX + "META-INF/logback/logback-";

    protected final static String CONFIG_FILE_URL_PATH_SUFFIX = FilenameUtils.EXTENSION_SEPARATOR + "groovy";

    protected AbstractLoggingInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        super(appContext);
    }

    @Override
    public boolean buildLogConsoleTerminal() {
        String consoleTerm = System.getProperty(ToolProperties.LOG_CONSOLE_TERM_NAME);

        return ((consoleTerm != null) ? BooleanUtils.toBoolean(consoleTerm) : (System.console() != null));
    }

    @Override
    public String buildLogFileName() {
        String logFileName =
            System.getProperty(ToolProperties.LOG_FILE_NAME_NAME, this.appContext.getEnvironment().getProperty(ToolProperties.APP_NAME_DISPLAY_NAME));

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

    public String buildLogDirectoryPath() {
        return System.getProperty(ToolProperties.LOG_DIR_NAME);
    }

    @Override
    public String buildLogContextName() {
        return System.getProperty(ToolProperties.LOG_CONTEXT_NAME_NAME, this.appContext.getEnvironment().getProperty(ToolProperties.APP_NAME_DISPLAY_NAME));
    }

    @Override
    public URL buildLogConfigFileUrl() {
        String logConfigFileUrlPath =
            CONFIG_FILE_URL_PATH_PREFIX + this.appContext.getEnvironment().getProperty(ToolProperties.APP_NAME_NAME) + CONFIG_FILE_URL_PATH_SUFFIX;

        try {
            return ResourceUtils.getURL(logConfigFileUrlPath);
        } catch (FileNotFoundException e) {
            throw new ToolApplicationContextException(String.format("Unable to determine Logback configuration file URL (path=%s).", logConfigFileUrlPath), e);
        }
    }
}
