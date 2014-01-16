package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.testcases.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;

public interface HostingTestcase extends ToolTestcase<HostingTestcaseDescription, HostingTestcaseResult> {
    public BindingType getBindingType();

    public void setBindingType(BindingType bindingType);

    public LocationType getLocationType();

    public void setLocationType(LocationType locType);
}
