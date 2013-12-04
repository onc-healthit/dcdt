package gov.hhs.onc.dcdt.testcases.hosting.impl;

import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseBinding;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseLocation;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
import java.util.List;

public class HostingTestcaseImpl extends AbstractToolTestcase<HostingTestcaseResult> implements HostingTestcase {
    private HostingTestcaseBinding binding;
    private HostingTestcaseLocation location;
    private String description;
    private String instructions;
    private String rtm;
    private List<String> specifications;

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
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getInstructions() {
        return this.instructions;
    }

    @Override
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String getRtm() {
        return this.rtm;
    }

    @Override
    public void setRtm(String rtm) {
        this.rtm = rtm;
    }

    @Override
    public List<String> getSpecifications() {
        return this.specifications;
    }

    @Override
    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }
}
