package gov.hhs.onc.dcdt.web.config;

/**
 * Encapsulates google analytics configuration information.
 */
public interface GoogleAnalyticsConfig {

    /**
     * Gets the status of google analytics enablement.
     * 
     * @return
     */
    boolean getEnabled();

    /**
     * Gets the google analytics property ID.
     * 
     * @return
     */
    String getId();

    /**
     * Gets the google analytics property URL.
     * 
     * @return
     */
    String getUrl();

}
