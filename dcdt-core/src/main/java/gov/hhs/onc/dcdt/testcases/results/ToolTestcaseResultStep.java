package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;

public interface ToolTestcaseResultStep extends ToolBean {
    public ToolTestcaseResultType getResultType();

    public void setResultType(ToolTestcaseResultType resultType);

    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);
}
