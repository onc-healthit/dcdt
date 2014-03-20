package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.net.utils.ToolUrlUtils;
import gov.hhs.onc.dcdt.service.mail.james.JamesResourcePatternResolver;
import gov.hhs.onc.dcdt.service.mail.james.ToolConfigurationProvider;
import gov.hhs.onc.dcdt.service.mail.james.ToolFileSystem;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ToolConfigurationProviderImpl extends AbstractToolBean implements ToolConfigurationProvider {
    public final static String FILE_EXT_CONF = ".conf";

    private AbstractApplicationContext appContext;
    private String configBuilderBeanName;
    private Map<String, String> configMappings;
    private ToolFileSystem fileSys;
    private Map<String, HierarchicalConfiguration> configMap = new HashMap<>();

    @Override
    public HierarchicalConfiguration getConfiguration(String beanConfigName) throws ConfigurationException {
        if (this.configMap.containsKey(beanConfigName)) {
            return this.configMap.get(beanConfigName);
        }

        JamesResourcePatternResolver resourcePatternResolver = this.fileSys.getResourcePatternResolver();

        String[] beanConfigNameParts = StringUtils.split(beanConfigName, ToolPropertyUtils.DELIM_PROP_NAME, 2);
        beanConfigNameParts[0] =
            StringUtils.appendIfMissing(
                StringUtils.prependIfMissing(StringUtils.stripStart(beanConfigNameParts[0], ToolUrlUtils.DELIM_URL),
                    (resourcePatternResolver.getConfDirectory() + ToolUrlUtils.DELIM_URL)), FILE_EXT_CONF);

        HierarchicalConfiguration beanConfig =
            ToolBeanFactoryUtils.createBean(
                this.appContext,
                this.configBuilderBeanName,
                CombinedConfiguration.class,
                ((Object) IteratorUtils.toList(ToolIteratorUtils.chainedIterator(ToolResourceUtils.mapLocations(resourcePatternResolver,
                    ToolResourceUtils.getOverrideableLocations(beanConfigNameParts[0])).values()))));

        if (beanConfigNameParts.length == 2) {
            beanConfig = beanConfig.configurationAt(beanConfigNameParts[1], true);
        }

        this.configMap.put(beanConfigName, beanConfig);

        return beanConfig;
    }

    @Override
    public void registerConfiguration(String beanName, HierarchicalConfiguration config) {
        this.configMap.put(beanName, config);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.hasConfigurationMappings()) {
            for (String beanName : this.configMappings.keySet()) {
                this.registerConfiguration(beanName, this.getConfiguration(this.configMappings.get(beanName)));
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public String getConfigurationBuilderBeanName() {
        return this.configBuilderBeanName;
    }

    @Override
    public void setConfigurationBuilderBeanName(String configBuilderBeanName) {
        this.configBuilderBeanName = configBuilderBeanName;
    }

    @Override
    public boolean hasConfigurationMappings() {
        return !MapUtils.isEmpty(this.configMappings);
    }

    @Nullable
    @Override
    public Map<String, String> getConfigurationMappings() {
        return this.configMappings;
    }

    @Override
    public void setConfigurationMappings(@Nullable Map<String, String> configMappings) {
        this.configMappings = configMappings;
    }

    @Override
    public ToolFileSystem getFileSystem() {
        return this.fileSys;
    }

    @Override
    public void setFileSystem(ToolFileSystem fileSys) {
        this.fileSys = fileSys;
    }
}
