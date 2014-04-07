package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import javax.annotation.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("namedBeanConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = ToolNamedBean.class) })
@List({ @Converts(from = String.class, to = ToolNamedBean.class) })
public class NamedBeanConverter extends AbstractToolConverter implements ApplicationContextAware {
    private final static TypeDescriptor TYPE_DESC_NAMED_BEAN = TypeDescriptor.valueOf(ToolNamedBean.class);

    private AbstractApplicationContext appContext;

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return sourceType.isAssignableTo(TYPE_DESC_NAMED_BEAN) ? ((ToolNamedBean) source).getName() : ToolBeanUtils.findNamed(this.appContext,
            ((String) source));
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
