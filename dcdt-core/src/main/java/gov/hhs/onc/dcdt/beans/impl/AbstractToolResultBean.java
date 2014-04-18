package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolResultBean;

public abstract class AbstractToolResultBean extends AbstractToolBean implements ToolResultBean {
    @Override
    public boolean hasMessages() {
        return !this.getMessages().isEmpty();
    }
}
