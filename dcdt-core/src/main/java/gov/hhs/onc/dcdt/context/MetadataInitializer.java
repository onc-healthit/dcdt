package gov.hhs.onc.dcdt.context;

import java.io.File;

public interface MetadataInitializer extends ToolInitializer {
    public File buildHomeDirectory();

    public String buildHomeDirectoryPath();

    public String buildName();

    public String buildNameDisplay();
}
