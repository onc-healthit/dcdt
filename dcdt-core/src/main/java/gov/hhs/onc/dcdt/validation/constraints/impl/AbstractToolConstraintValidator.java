package gov.hhs.onc.dcdt.validation.constraints.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.validation.constraints.ToolConstraintValidator;
import java.lang.annotation.Annotation;
import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

public abstract class AbstractToolConstraintValidator<T extends Annotation, U> extends AbstractToolBean implements ToolConstraintValidator<T, U> {
    @Resource(name = "validatorFactory")
    protected ValidatorFactory validatorFactory;

    @Resource(name = "messageSourceValidation")
    protected MessageSource msgSourceValidation;

    protected T constraintAnno;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolConstraintValidator.class);

    @Override
    public boolean isValid(U value, ConstraintValidatorContext validatorContext) {
        return true;
    }

    @Override
    public void initialize(T constraintAnno) {
        this.constraintAnno = constraintAnno;

        LOGGER.trace(String.format("Tool constraint validator (class=%s) initialized with annotation (class=%s).", ToolClassUtils.getName(this),
            ToolClassUtils.getName(this.constraintAnno)));
    }
}
