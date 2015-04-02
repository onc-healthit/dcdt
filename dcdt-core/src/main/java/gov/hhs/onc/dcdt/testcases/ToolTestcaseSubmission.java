package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolDirectAddressBean;
import javax.annotation.Nullable;

public interface ToolTestcaseSubmission<T extends ToolTestcaseDescription, U extends ToolTestcase<T>> extends ToolDirectAddressBean {
    public boolean hasTestcase();

    @Nullable
    public U getTestcase();

    public void setTestcase(@Nullable U testcase);
}
