package gov.hhs.onc.dcdt.testcases;


import gov.hhs.onc.dcdt.beans.ToolBean;

public interface ToolTestcase<T extends ToolTestcaseResult> extends ToolBean {
    public String getMailAddress();

    public void setMailAddress(String mailAddr);

    public String getName();

    public void setName(String name);

    public String getNameDisplay();

    public void setNameDisplay(String nameDisplay);

    public boolean isOptional();

    public void setOptional(boolean optional);

    public T getResult();

    public void setResult(T result);
}
