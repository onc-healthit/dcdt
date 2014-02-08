package gov.hhs.onc.dcdt.testcases.impl;

import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.ToolTestcaseResultJsonDto;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolTestcaseResultJsonDto<T extends ToolTestcaseResult> extends AbstractToolBeanJsonDto<T> implements
    ToolTestcaseResultJsonDto<T> {
    protected AbstractToolTestcaseResultJsonDto(Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(beanClass, beanImplClass);
    }
}
