package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseBinding;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseLocation;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;

public class HostingTestcaseImpl extends AbstractToolTestcase<HostingTestcaseResult, HostingTestcaseDescription> implements HostingTestcase {
    private HostingTestcaseBinding binding;
    private HostingTestcaseLocation location;
    private HostingTestcaseDescription hostingTestcaseDescription;

    @Override
    public HostingTestcaseBinding getBinding() {
        return this.binding;
    }

    @Override
    public void setBinding(HostingTestcaseBinding binding) {
        this.binding = binding;
    }

    @Override
    public HostingTestcaseLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(HostingTestcaseLocation location) {
        this.location = location;
    }

    @Override
    public HostingTestcaseDescription getHostingTestcaseDescription() {
        return this.hostingTestcaseDescription;
    }

    @Override
    public void setHostingTestcaseDescription(HostingTestcaseDescription hostingTestcaseDescription) {
        this.hostingTestcaseDescription = hostingTestcaseDescription;
    }
}
