package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractToolTestcaseDescription extends AbstractToolBean implements ToolTestcaseDescription {
    protected String text;
    protected String instructions;
    protected String rtm;
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
    public boolean hasRtm() {
        return !StringUtils.isBlank(this.rtm);
    }

    @Nullable
    @Override
    public String getRtm() {
        return this.rtm;
    }

    @Override
    public void setRtm(String rtm) {
        this.rtm = rtm;
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

    @Override
    public boolean hasText() {
        return !StringUtils.isBlank(this.text);
    }

    @Nullable
    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }
}
