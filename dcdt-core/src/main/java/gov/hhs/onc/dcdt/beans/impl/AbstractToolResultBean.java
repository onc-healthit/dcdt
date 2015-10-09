package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.ToolResultBean;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractToolResultBean extends AbstractToolBean implements ToolResultBean {
    @Override
    public boolean hasMessages(ToolMessageLevel level) {
        return (this.hasMessages() && this.getMessages().stream().anyMatch(msg -> (msg.getLevel() == level)));
    }

    @Override
    public boolean hasMessages() {
        return !this.getMessages().isEmpty();
    }

    @Override
    public List<ToolMessage> getMessages(ToolMessageLevel level) {
        return this.getMessages().stream().filter(msg -> (msg.getLevel() == level)).collect(Collectors.toList());
    }

    @Override
    public boolean isSuccess() {
        return !this.hasMessages(ToolMessageLevel.ERROR);
    }
}
