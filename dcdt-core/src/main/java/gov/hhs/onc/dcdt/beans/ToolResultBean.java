package gov.hhs.onc.dcdt.beans;

import java.util.List;

public interface ToolResultBean extends ToolBean {
    public boolean hasMessages(ToolMessageLevel level);

    public boolean hasMessages();

    public List<ToolMessage> getMessages(ToolMessageLevel level);

    public List<ToolMessage> getMessages();

    public boolean isSuccess();
}
