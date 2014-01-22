package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import java.util.List;
import javax.annotation.Nullable;

public interface ToolTestcaseDescription extends ToolDescriptionBean {
    public boolean hasInstructions();

    @JsonProperty("instructions")
    @Nullable
    public String getInstructions();

    public void setInstructions(@Nullable String instructions);

    public boolean hasRtmSections();

    @JsonProperty("rtmSections")
    @Nullable
    public List<String> getRtmSections();

    public void setRtmSections(@Nullable List<String> rtmSections);

    public boolean hasSpecifications();

    @JsonProperty("specs")
    @Nullable
    public List<String> getSpecifications();

    public void setSpecifications(@Nullable List<String> specification);
}
