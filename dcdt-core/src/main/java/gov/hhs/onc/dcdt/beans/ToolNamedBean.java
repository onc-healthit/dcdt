package gov.hhs.onc.dcdt.beans;

import javax.annotation.Nullable;

public interface ToolNamedBean extends ToolBean {
    public boolean hasName();

    @Nullable
    public String getName();

    public void setName(@Nullable String name);

    public boolean hasNameDisplay();

    @Nullable
    public String getNameDisplay();

    public void setNameDisplay(@Nullable String nameDisplay);
}
