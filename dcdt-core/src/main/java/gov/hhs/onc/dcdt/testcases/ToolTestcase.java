package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import javax.annotation.Nullable;

public interface ToolTestcase<T extends ToolTestcaseDescription, U extends ToolTestcaseResult> extends ToolNamedBean {
    public boolean hasDescription();

    @Nullable
    public T getDescription();

    public void setDescription(@Nullable T desc);

    public boolean isOptional();

    public void setOptional(boolean optional);

    public boolean hasResult();

    @Nullable
    public U getResult();

    public void setResult(@Nullable U result);
}
