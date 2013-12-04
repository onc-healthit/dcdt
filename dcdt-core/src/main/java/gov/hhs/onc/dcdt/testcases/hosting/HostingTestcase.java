package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import java.util.List;

public interface HostingTestcase extends ToolTestcase<HostingTestcaseResult> {
    public HostingTestcaseBinding getBinding();

    public void setBinding(HostingTestcaseBinding binding);

    public HostingTestcaseLocation getLocation();

    public void setLocation(HostingTestcaseLocation location);

    public String getDescription();

    public void setDescription(String description);

    public String getInstructions();

    public void setInstructions(String instructions);

    public String getRtm();

    public void setRtm(String rtm);

    public List<String> getSpecifications();

    public void setSpecifications(List<String> specifications);
}
