package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;

@JsonSubTypes({ @Type(ToolMessageImpl.class) })
public interface ToolMessage extends ToolBean {
    @JsonProperty
    public ToolMessageLevel getLevel();

    public void setLevel(ToolMessageLevel level);

    @JsonProperty
    public String getText();

    public void setText(String text);
}
