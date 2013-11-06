package gov.hhs.onc.dcdt.testcases;


import gov.hhs.onc.dcdt.beans.ToolBean;

public interface ToolTestcaseResult extends ToolBean {
    public boolean isPassed();

    public void setPassed(boolean passed);
}
