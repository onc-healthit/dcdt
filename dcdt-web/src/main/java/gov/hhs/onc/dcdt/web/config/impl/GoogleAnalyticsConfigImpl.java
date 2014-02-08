package gov.hhs.onc.dcdt.web.config.impl;

import gov.hhs.onc.dcdt.web.config.GoogleAnalyticsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("googleAnalyticsConfigImpl")
public class GoogleAnalyticsConfigImpl implements GoogleAnalyticsConfig {
    @Value("${dcdt.web.google.analytics.enabled}")
    private boolean enabled;

    @Value("${dcdt.web.google.analytics.id}")
    private String id;

    @Value("${dcdt.web.google.analytics.url}")
    private String url;

    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}
