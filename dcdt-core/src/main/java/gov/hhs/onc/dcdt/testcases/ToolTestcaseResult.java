package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;

public interface ToolTestcaseResult extends ToolBean {
    public boolean isPassed();

    public void setPassed(boolean passed);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);
}
