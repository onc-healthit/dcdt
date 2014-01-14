package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.List;
import javax.annotation.Nullable;

public interface ToolTestcaseDescription extends ToolBean {
    public boolean hasText();

    @Nullable
    public String getText();

    public void setText(@Nullable String text);

    public boolean hasInstructions();

    @Nullable
    public String getInstructions();

    public void setInstructions(@Nullable String instructions);

    public boolean hasRtm();

    @Nullable
    public String getRtm();

    public void setRtm(@Nullable String rtm);

    public boolean hasSpecifications();

    @Nullable
    public List<String> getSpecifications();

    public void setSpecifications(@Nullable List<String> specification);
}
