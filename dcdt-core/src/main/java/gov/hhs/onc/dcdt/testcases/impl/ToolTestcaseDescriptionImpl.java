package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import java.util.List;

public abstract class ToolTestcaseDescriptionImpl implements ToolTestcaseDescription {
    private String description;
    private String instructions;
    private String rtm;
    private List<String> specifications;

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
