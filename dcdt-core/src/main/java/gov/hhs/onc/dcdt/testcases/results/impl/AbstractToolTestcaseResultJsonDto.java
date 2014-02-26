package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolTestcaseResultJsonDto<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo, V extends ToolTestcaseResult<T, U>>
    extends AbstractToolBeanJsonDto<V> implements ToolTestcaseResultJsonDto<T, U, V> {
    protected AbstractToolTestcaseResultJsonDto(Class<V> beanClass, Class<? extends V> beanImplClass) {
        super(beanClass, beanImplClass);
    }
}
