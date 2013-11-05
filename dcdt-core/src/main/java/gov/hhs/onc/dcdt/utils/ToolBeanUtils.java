package gov.hhs.onc.dcdt.utils;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolBeanException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract class ToolBeanUtils {
    public static <T extends ToolBean> T clone(BeanFactory beanFactory, T bean) throws ToolBeanException {
        try {
            return (T) beanFactory.getBean(bean.getBeanName(), bean);
        } catch (ClassCastException | BeansException e) {
            throw new ToolBeanException("Unable to clone bean (class=" + ToolClassUtils.getName(bean) + ").", e);
        }
    }
}
