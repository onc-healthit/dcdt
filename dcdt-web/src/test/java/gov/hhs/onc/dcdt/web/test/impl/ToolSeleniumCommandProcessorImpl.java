package gov.hhs.onc.dcdt.web.test.impl;

import com.thoughtworks.selenium.HttpCommandProcessor;
import gov.hhs.onc.dcdt.web.test.ToolSeleniumCommandProcessor;
import java.beans.ConstructorProperties;

public class ToolSeleniumCommandProcessorImpl extends HttpCommandProcessor implements ToolSeleniumCommandProcessor {
    @ConstructorProperties({ "serverHost", "serverPort", "browserStartCmd", "browserUrl" })
    public ToolSeleniumCommandProcessorImpl(String serverHost, int serverPort, String browserStartCmd, String browserUrl) {
        super(serverHost, serverPort, browserStartCmd, browserUrl);
    }
}
