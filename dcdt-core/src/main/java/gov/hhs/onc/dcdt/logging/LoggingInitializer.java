package gov.hhs.onc.dcdt.logging;

import gov.hhs.onc.dcdt.context.ToolInitializer;
import java.io.File;
import java.net.URL;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public interface LoggingInitializer<T extends AbstractRefreshableConfigApplicationContext> extends ToolInitializer<T> {
    public boolean buildLogConsoleTerminal();

    public String buildLogFileName();

    public File buildLogDirectory();

    public String buildLogDirectoryPath();

    public String buildLogContextName();

    public URL buildLogConfigFileUrl();
}
