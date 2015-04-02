package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseSubmissionJsonDto;
import javax.annotation.Nullable;

public abstract class AbstractToolTestcaseSubmissionJsonDto<T extends ToolTestcaseDescription, U extends ToolTestcase<T>, V extends ToolTestcaseSubmission<T, U>>
    extends AbstractToolBeanJsonDto<V> implements ToolTestcaseSubmissionJsonDto<T, U, V> {
    protected String testcase;

    protected AbstractToolTestcaseSubmissionJsonDto(Class<V> beanClass, Class<? extends V> beanImplClass) {
        super(beanClass, beanImplClass);
    }

    @Nullable
    @Override
    public String getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable String testcase) {
        this.testcase = testcase;
    }
}
