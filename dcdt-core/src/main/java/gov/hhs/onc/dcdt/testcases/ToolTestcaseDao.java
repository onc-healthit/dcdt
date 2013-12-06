package gov.hhs.onc.dcdt.testcases;

import gov.hhs.onc.dcdt.beans.ToolBeanDao;

public interface ToolTestcaseDao<T extends ToolTestcaseResult, U extends ToolTestcaseDescription, V extends ToolTestcase<T, U>> extends ToolBeanDao<V> {
}
