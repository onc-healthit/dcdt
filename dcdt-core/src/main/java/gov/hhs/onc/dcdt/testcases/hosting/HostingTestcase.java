package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.testcases.ToolTestcase;

public interface HostingTestcase extends ToolTestcase<HostingTestcaseDescription, HostingTestcaseResult> {
    public HostingTestcaseBinding getBinding();

    public void setBinding(HostingTestcaseBinding binding);

    public HostingTestcaseLocation getLocation();

    public void setLocation(HostingTestcaseLocation location);
}
