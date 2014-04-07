package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolDirectAddressBean;
import javax.annotation.Nullable;

public interface ToolTestcaseSubmission<T extends ToolTestcaseDescription, U extends ToolTestcaseConfig, V extends ToolTestcase<T, U>> extends
    ToolDirectAddressBean {
    public boolean hasTestcase();

    @Nullable
    public V getTestcase();

    public void setTestcase(@Nullable V testcase);
}
