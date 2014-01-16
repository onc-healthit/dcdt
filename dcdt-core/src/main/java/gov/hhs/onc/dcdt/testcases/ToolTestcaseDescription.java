package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import java.util.List;
import javax.annotation.Nullable;

public interface ToolTestcaseDescription extends ToolDescriptionBean {
    public boolean hasInstructions();

    @Nullable
    public String getInstructions();

    public void setInstructions(@Nullable String instructions);

    public boolean hasRtmSections();

    @Nullable
    public List<String> getRtmSections();

    public void setRtmSections(@Nullable List<String> rtmSections);

    public boolean hasSpecifications();

    @Nullable
    public List<String> getSpecifications();

    public void setSpecifications(@Nullable List<String> specification);
}
