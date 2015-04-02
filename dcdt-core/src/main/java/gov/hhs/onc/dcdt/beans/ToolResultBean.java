package gov.hhs.onc.dcdt.beans;

import java.util.List;

public interface ToolResultBean extends ToolBean {
    public boolean hasMessages();

    public List<String> getMessages();

    public boolean isSuccess();
}
