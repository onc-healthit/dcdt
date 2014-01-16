package gov.hhs.onc.dcdt.beans;

import javax.annotation.Nullable;

public interface ToolDescriptionBean extends ToolBean {
    public boolean hasText();

    @Nullable
    public String getText();

    public void setText(@Nullable String text);
}
