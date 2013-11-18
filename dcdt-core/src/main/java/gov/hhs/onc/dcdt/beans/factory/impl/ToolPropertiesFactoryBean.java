package gov.hhs.onc.dcdt.beans.factory.impl;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

public class ToolPropertiesFactoryBean extends PropertiesFactoryBean {
    @SuppressWarnings({ "serial" })
    private class ToolProperties extends Properties {
        public ToolProperties(Properties props) {
            for (String propName : props.stringPropertyNames()) {
                if (namePattern.matcher(propName).matches()) {
                    this.setProperty(propName, props.getProperty(propName));
                }
            }
        }
    }
    
    private Pattern namePattern;
    
    @Override
    protected Properties createProperties() throws IOException {
        Properties props = super.createProperties();
        
        return (this.namePattern != null) ? new ToolProperties(props) : props;
    }

    public Pattern getNamePattern() {
        return this.namePattern;
    }

    public void setNamePattern(Pattern namePattern) {
        this.namePattern = namePattern;
    }
}
