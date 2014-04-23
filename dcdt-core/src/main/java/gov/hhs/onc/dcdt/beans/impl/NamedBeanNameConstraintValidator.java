package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.NamedBeanName;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolStringConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class NamedBeanNameConstraintValidator extends AbstractToolStringConstraintValidator<NamedBeanName> implements ApplicationContextAware {
    private AbstractApplicationContext appContext;

    @Override
    protected boolean isValidInternal(String value, ConstraintValidatorContext validatorContext) throws Exception {
        return (ToolBeanUtils.findNamed(this.appContext, value, this.anno.value()) != null);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
