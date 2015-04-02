package gov.hhs.onc.dcdt.json.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.data.dto.impl.AbstractToolBeanDto;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanJsonDto<T extends ToolBean> extends AbstractToolBeanDto<T> {
    protected AbstractToolBeanJsonDto(Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(beanClass, beanImplClass);
    }

    @Override
    public T toBean(ConversionService convService) throws Exception {
        T bean = BeanUtils.instantiate(this.beanImplClass);

        ToolBeanPropertyUtils.copy(ToolBeanUtils.wrap(this, convService), ToolBeanUtils.wrap(bean, convService));

        return bean;
    }

    @Override
    public void fromBean(ConversionService convService, T bean) throws Exception {
        ToolBeanPropertyUtils.copy(ToolBeanUtils.wrap(bean, convService), ToolBeanUtils.wrap(this, convService));
    }
}
