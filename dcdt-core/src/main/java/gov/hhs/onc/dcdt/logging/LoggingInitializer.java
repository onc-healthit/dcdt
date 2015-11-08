package gov.hhs.onc.dcdt.logging;

import gov.hhs.onc.dcdt.context.ToolInitializer;
import java.io.File;
import java.net.URL;

public interface LoggingInitializer extends ToolInitializer {
    public boolean buildLogConsoleTerminal();

    public String buildLogFileName();

    public File buildLogDirectory();

    public String buildLogDirectoryPath();

    public String buildLogContextName();

    public URL buildLogConfigFileUrl();
}
