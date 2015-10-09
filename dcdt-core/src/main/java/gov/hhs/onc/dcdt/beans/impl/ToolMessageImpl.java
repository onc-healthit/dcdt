package gov.hhs.onc.dcdt.beans.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.ToolMessage;

@JsonTypeName("msg")
public class ToolMessageImpl extends AbstractToolBean implements ToolMessage {
    private ToolMessageLevel level;
    private String text;

    public ToolMessageImpl(ToolMessageLevel level, String text) {
        this.level = level;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.level.name(), this.text);
    }

    @Override
    public ToolMessageLevel getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(ToolMessageLevel level) {
        this.level = level;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
