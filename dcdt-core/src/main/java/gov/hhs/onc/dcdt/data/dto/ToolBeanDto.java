package gov.hhs.onc.dcdt.data.dto;

import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.core.convert.ConversionService;

public interface ToolBeanDto<T extends ToolBean> extends ToolBean {
    public T toBean(ConversionService convService) throws Exception;

    public void fromBean(ConversionService convService, T bean) throws Exception;

    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();
}
