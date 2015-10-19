package gov.hhs.onc.dcdt.logging;

import java.io.File;
import java.net.URL;

public interface LoggingInitializer {
    public String buildLogFileName();

    public File buildLogDirectory();

    public String buildLogContextName();

    public URL buildLogConfigFileUrl();
}
