package gov.hhs.onc.dcdt.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("googleAnalyticsConfigurationBean")
@Lazy
@Scope("singleton")
public class GoogleAnalyticsConfigurationBean {

    @Value("${dcdt.google.analytics.enabledStatus}")
    private String enabledStatus;

    @Value("${dcdt.google.analytics.id}")
    private String id;

    @Value("${dcdt.google.analytics.url}")
    private String url;

    public String getUrl() {
        return url;
    }

    public String getEnabledStatus() {
        return enabledStatus;
    }

    public String getId() {
        return id;
    }

}
