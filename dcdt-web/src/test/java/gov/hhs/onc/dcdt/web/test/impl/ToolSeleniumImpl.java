package gov.hhs.onc.dcdt.web.test.impl;


import com.thoughtworks.selenium.DefaultSelenium;
import gov.hhs.onc.dcdt.web.test.ToolSelenium;
import gov.hhs.onc.dcdt.web.test.ToolSeleniumCommandProcessor;
import org.apache.commons.lang3.StringUtils;

public class ToolSeleniumImpl extends DefaultSelenium implements ToolSelenium {
    public ToolSeleniumImpl(ToolSeleniumCommandProcessor seleniumCmdProc) {
        super(seleniumCmdProc);
    }

    @Override
    public void open() {
        this.open(StringUtils.EMPTY);
    }
}
