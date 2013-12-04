package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.ToolMessageSource;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

public class ToolMessageSourceImpl extends ReloadableResourceBundleMessageSource implements ToolMessageSource {
    @SuppressWarnings({ "serial" })
    private static class ToolMessageSourceProperties extends Properties {
        public ToolMessageSourceProperties(Properties props) {
            super(props);
        }

        @Override
        public String getProperty(String key) {
            String value = super.getProperty(key);

            return (value != null) ? StrSubstitutor.replace(value, this.defaults) : null;
        }
    }

    @Override
    protected Properties loadProperties(Resource resource, String fileName) throws IOException {
        return new ToolMessageSourceProperties(super.loadProperties(resource, fileName));
    }
}
