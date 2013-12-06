package gov.hhs.onc.dcdt.testcases;

import java.util.List;

public interface ToolTestcaseDescription {
    public String getDescription();

    public void setDescription(String description);

    public String getInstructions();

    public void setInstructions(String instructions);

    public String getRtm();

    public void setRtm(String rtm);

    public List<String> getSpecifications();

    public void setSpecifications(List<String> specification);
}
