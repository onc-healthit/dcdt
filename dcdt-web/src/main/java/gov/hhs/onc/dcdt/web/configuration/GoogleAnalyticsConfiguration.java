package gov.hhs.onc.dcdt.web.configuration;

/**
 *  Encapsulates google analytics configuration information.
 */
public interface GoogleAnalyticsConfiguration {

    /**
     * Gets the status of google analytics enablement, either "false" or "true".
     * @return
     */
    String getEnabledStatus();

    /**
     * Gets the google analytics property ID.
     * @return
     */
    String getId();

    /**
     * Gets the google analytics property URL.
     * @return
     */
    String getUrl();

}
