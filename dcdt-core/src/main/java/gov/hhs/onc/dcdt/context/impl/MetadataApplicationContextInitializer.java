package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.MetadataInitializer;
import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.context.utils.ToolContextUtils;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MapPropertySource;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetadataApplicationContextInitializer extends AbstractToolApplicationContextInitializer {
    private static class DefaultMetadataInitializer extends AbstractMetadataInitializer {
        public DefaultMetadataInitializer(AbstractRefreshableConfigApplicationContext appContext) {
            super(appContext, "core", "dcdt-core");
        }
    }

    @Override
    protected void initializeInternal(AbstractRefreshableConfigApplicationContext appContext) throws Exception {
        MetadataInitializer metadataInit =
            ToolContextUtils.buildComponent(MetadataInitializer.class, () -> new DefaultMetadataInitializer(appContext), appContext);
        Map<String, Object> props = new LinkedHashMap<>(3);

        File homeDir = metadataInit.buildHomeDirectory();
        props.put(ToolProperties.APP_HOME_DIR_NAME, homeDir);

        props.put(ToolProperties.APP_NAME_NAME, metadataInit.buildName());
        props.put(ToolProperties.APP_NAME_DISPLAY_NAME, metadataInit.buildNameDisplay());

        appContext.getEnvironment().getPropertySources().addFirst(new MapPropertySource(MetadataInitializer.class.getName(), props));
    }
}
