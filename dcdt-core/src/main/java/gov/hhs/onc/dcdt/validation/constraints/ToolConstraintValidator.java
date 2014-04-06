package gov.hhs.onc.dcdt.validation.constraints;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public interface ToolConstraintValidator<T extends Annotation, U> extends ConstraintValidator<T, U>, ToolBean {
    @Override
    public boolean isValid(@Nullable U value, ConstraintValidatorContext validatorContext);
}
