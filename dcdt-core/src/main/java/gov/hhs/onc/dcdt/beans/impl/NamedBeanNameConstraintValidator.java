package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.NamedBeanName;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class NamedBeanNameConstraintValidator extends AbstractToolConstraintValidator<NamedBeanName, String> implements ApplicationContextAware {
    private AbstractApplicationContext appContext;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext validatorContext) {
        return ToolBeanUtils.findNamed(this.appContext, value, this.constraintAnno.value()) != null;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
