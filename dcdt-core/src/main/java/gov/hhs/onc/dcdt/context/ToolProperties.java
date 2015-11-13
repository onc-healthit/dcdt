package gov.hhs.onc.dcdt.context;

public final class ToolProperties {
    public final static String PREFIX = "dcdt.";
    public final static String APP_PREFIX = PREFIX + "app.";
    public final static String CATALINA_PREFIX = "catalina.";
    public final static String LOG_PREFIX = PREFIX + "log.";
    public final static String LOG_CONSOLE_PREFIX = LOG_PREFIX + "console.";
    public final static String LOG_CONTEXT_PREFIX = LOG_PREFIX + "context.";
    public final static String LOG_FILE_PREFIX = LOG_PREFIX + "file.";

    public final static String DIR_SUFFIX = "dir";
    public final static String NAME_SUFFIX = "name";
    public final static String PATTERN_SUFFIX = "pattern";

    public final static String APP_HOME_DIR_NAME = APP_PREFIX + "home." + DIR_SUFFIX;
    public final static String APP_NAME_NAME = APP_PREFIX + NAME_SUFFIX;
    public final static String APP_NAME_DISPLAY_NAME = APP_NAME_NAME + ".display";

    public final static String CATALINA_BASE_NAME = CATALINA_PREFIX + "base";

    public final static String LOG_CONTEXT_NAME_NAME = LOG_CONTEXT_PREFIX + NAME_SUFFIX;
    public final static String LOG_CONTEXT_INITIALIZED_NAME = LOG_CONTEXT_PREFIX + "initialized";

    public final static String LOG_CONSOLE_PATTERN_NAME = LOG_CONSOLE_PREFIX + PATTERN_SUFFIX;
    public final static String LOG_CONSOLE_TARGET_NAME = LOG_CONSOLE_PREFIX + "target";
    public final static String LOG_CONSOLE_TERM_NAME = LOG_CONSOLE_PREFIX + "term";

    public final static String LOG_DIR_NAME = LOG_PREFIX + DIR_SUFFIX;
    public final static String LOG_FILE_PATTERN_NAME = LOG_FILE_PREFIX + PATTERN_SUFFIX;
    public final static String LOG_FILE_EXT_NAME = LOG_FILE_PREFIX + "ext";
    public final static String LOG_FILE_NAME_NAME = LOG_FILE_PREFIX + NAME_SUFFIX;
    public final static String LOG_FILE_SIZE_MAX_NAME = LOG_FILE_PREFIX + "size.max";

    private ToolProperties() {
    }
}
