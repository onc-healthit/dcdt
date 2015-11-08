package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.MetadataInitializer;
import gov.hhs.onc.dcdt.context.ToolProperties;
import java.io.File;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public abstract class AbstractMetadataInitializer extends AbstractToolInitializer implements MetadataInitializer {
    protected String name;
    protected String nameDisplay;

    protected AbstractMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext, String name, String nameDisplay) {
        super(appContext);

        this.name = name;
        this.nameDisplay = nameDisplay;
    }

    @Override
    public File buildHomeDirectory() {
        return new File(this.buildHomeDirectoryPath());
    }

    @Override
    public String buildHomeDirectoryPath() {
        return this.appContext.getEnvironment().getProperty(ToolProperties.APP_HOME_DIR_NAME, SystemUtils.USER_DIR);
    }

    @Override
    public String buildName() {
        return this.name;
    }

    @Override
    public String buildNameDisplay() {
        return this.nameDisplay;
    }
}
