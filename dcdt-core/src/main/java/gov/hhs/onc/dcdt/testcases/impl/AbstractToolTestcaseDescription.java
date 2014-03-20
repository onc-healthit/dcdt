package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractToolTestcaseDescription extends AbstractToolDescriptionBean implements ToolTestcaseDescription {
    protected String instructions;
    protected List<String> rtmSections;
    protected List<String> specifications;

    @Override
    public boolean hasInstructions() {
        return !StringUtils.isBlank(this.instructions);
    }

    @Nullable
    @Override
    public String getInstructions() {
        return this.instructions;
    }

    @Override
    public void setInstructions(@Nullable String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean hasRtmSections() {
        return !CollectionUtils.isEmpty(this.rtmSections);
    }

    @Nullable
    @Override
    public List<String> getRtmSections() {
        return this.rtmSections;
    }

    @Override
    public void setRtmSections(@Nullable List<String> rtmSections) {
        this.rtmSections = rtmSections;
    }

    @Override
    public boolean hasSpecifications() {
        return !CollectionUtils.isEmpty(this.specifications);
    }

    @Nullable
    @Override
    public List<String> getSpecifications() {
        return this.specifications;
    }

    @Override
    public void setSpecifications(@Nullable List<String> specifications) {
        this.specifications = specifications;
    }
}
