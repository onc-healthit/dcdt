package gov.hhs.onc.dcdt.testcases;


import gov.hhs.onc.dcdt.beans.ToolBeanDao;

public interface ToolTestcaseDao<T extends ToolTestcaseResult, U extends ToolTestcase<T>> extends ToolBeanDao<U> {
}
