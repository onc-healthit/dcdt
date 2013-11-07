package gov.hhs.onc.dcdt.web.test.impl;


import com.thoughtworks.selenium.DefaultSelenium;
import gov.hhs.onc.dcdt.web.test.ToolSelenium;
import java.beans.ConstructorProperties;

public class ToolSeleniumImpl extends DefaultSelenium implements ToolSelenium {
    @ConstructorProperties({ "serverHost", "serverPort", "browserStartCommand", "browserURL" })
    public ToolSeleniumImpl(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
        super(serverHost, serverPort, browserStartCommand, browserURL);
    }
}
