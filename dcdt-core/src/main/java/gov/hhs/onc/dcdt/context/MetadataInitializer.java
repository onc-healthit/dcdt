package gov.hhs.onc.dcdt.context;

import java.io.File;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public interface MetadataInitializer<T extends AbstractRefreshableConfigApplicationContext> extends ToolInitializer<T> {
    public File buildHomeDirectory();

    public String buildHomeDirectoryPath();

    public String buildName();

    public String buildNameDisplay();
}
