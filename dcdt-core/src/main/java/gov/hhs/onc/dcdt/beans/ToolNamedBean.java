package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

public interface ToolNamedBean extends ToolBean {
    public boolean hasName();

    @JsonProperty("name")
    @Nullable
    public String getName();

    public void setName(@Nullable String name);

    public boolean hasNameDisplay();

    @JsonProperty("nameDisplay")
    @Nullable
    public String getNameDisplay();

    public void setNameDisplay(@Nullable String nameDisplay);
}
