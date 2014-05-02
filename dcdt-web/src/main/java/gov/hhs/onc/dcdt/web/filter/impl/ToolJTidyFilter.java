package gov.hhs.onc.dcdt.web.filter.impl;

import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.tidy.servlet.filter.JTidyFilter;

public class ToolJTidyFilter extends AbstractContentFilter<JTidyFilter> {
    private final static String DELIM_CONFIG_ENTRY = "; ";
    private final static String DELIM_CONFIG_ENTRY_PAIR = ": ";

    private Map<String, Object> config = new LinkedHashMap<>();

    public ToolJTidyFilter() {
        super(new JTidyFilter());
    }

    @Override
    protected MutableFilterConfig initInternal(MutableFilterConfig filterConfig) throws ServletException {
        ToolStrBuilder configStrBuilder = new ToolStrBuilder();

        for (String configPropName : this.config.keySet()) {
            configStrBuilder.appendDelimiter(DELIM_CONFIG_ENTRY);
            configStrBuilder.append(configPropName);
            configStrBuilder.appendDelimiter(DELIM_CONFIG_ENTRY_PAIR);
            configStrBuilder.append(Objects.toString(this.config.get(configPropName), StringUtils.EMPTY));
        }

        filterConfig.getInitParameters().put(JTidyFilter.CONFIG_CONFIG, configStrBuilder.build());

        return super.initInternal(filterConfig);
    }

    public Map<String, Object> getConfig() {
        return this.config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config.clear();
        this.config.putAll(config);
    }
}
