package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;

public interface ToolTestcase<T extends ToolTestcaseDescription, U extends ToolTestcaseResult> extends ToolBean {
    public T getDescription();

    public void setDescription(T desc);

    public String getName();

    public void setName(String name);

    public String getNameDisplay();

    public void setNameDisplay(String nameDisplay);

    public boolean isOptional();

    public void setOptional(boolean optional);

    public U getResult();

    public void setResult(U result);
}
