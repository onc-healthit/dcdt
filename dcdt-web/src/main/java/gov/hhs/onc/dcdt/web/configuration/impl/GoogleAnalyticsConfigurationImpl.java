package gov.hhs.onc.dcdt.web.configuration.impl;

import gov.hhs.onc.dcdt.web.configuration.GoogleAnalyticsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("googleAnalyticsConfiguration")
@Lazy
@Scope("singleton")
public class GoogleAnalyticsConfigurationImpl implements GoogleAnalyticsConfiguration {

    @Value("${dcdt.google.analytics.enabledStatus}")
    private String enabledStatus;

    @Value("${dcdt.google.analytics.id}")
    private String id;

    @Value("${dcdt.google.analytics.url}")
    private String url;

    @Override
    public String getEnabledStatus() {
        return enabledStatus;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return url;
    }

}
