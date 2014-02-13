package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResult;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultJsonDto;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolTestcaseResultJsonDto<T extends ToolTestcaseResultConfig, U extends ToolTestcaseResultInfo, V extends ToolTestcaseResult<T, U>>
    extends AbstractToolBeanJsonDto<V> implements ToolTestcaseResultJsonDto<T, U, V> {
    protected boolean successful;
    protected String message;

    protected AbstractToolTestcaseResultJsonDto(Class<V> beanClass, Class<? extends V> beanImplClass) {
        super(beanClass, beanImplClass);
    }

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.message);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
