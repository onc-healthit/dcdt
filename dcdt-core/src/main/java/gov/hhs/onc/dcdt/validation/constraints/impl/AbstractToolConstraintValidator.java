package gov.hhs.onc.dcdt.validation.constraints.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import gov.hhs.onc.dcdt.validation.constraints.ToolConstraintValidator;
import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidatorFactory;
import org.springframework.context.MessageSource;

public abstract class AbstractToolConstraintValidator<T extends Annotation, U> extends AbstractToolBean implements ToolConstraintValidator<T, U> {
    @Resource(name = "validatorFactory")
    protected ValidatorFactory validatorFactory;

    @Resource(name = "messageSourceValidation")
    protected MessageSource msgSourceValidation;

    protected Class<U> valueClass;
    protected T anno;

    protected AbstractToolConstraintValidator(Class<U> valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    public boolean isValid(@Nullable U value, ConstraintValidatorContext validatorContext) {
        if (!this.canValidate(value, validatorContext)) {
            return true;
        }

        try {
            return this.isValidInternal(value, validatorContext);
        } catch (Exception e) {
            validatorContext.disableDefaultConstraintViolation();
            validatorContext.buildConstraintViolationWithTemplate(ToolValidationUtils.escapeMessageMacros(e.getMessage())).addConstraintViolation();

            return false;
        }
    }

    @Override
    public void initialize(T anno) {
        this.anno = anno;
    }

    protected abstract boolean isValidInternal(U value, ConstraintValidatorContext validatorContext) throws Exception;

    protected boolean canValidate(@Nullable U value, ConstraintValidatorContext validatorContext) {
        return ToolClassUtils.isAssignable(ToolClassUtils.getClass(value), this.valueClass);
    }
}
