package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

public interface ToolDescriptionBean extends ToolBean {
    public boolean hasText();

    @JsonProperty("text")
    @Nullable
    public String getText();

    public void setText(@Nullable String text);
}
