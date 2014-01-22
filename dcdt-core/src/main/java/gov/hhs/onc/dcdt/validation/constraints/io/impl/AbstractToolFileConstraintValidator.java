package gov.hhs.onc.dcdt.validation.constraints.io.impl;

import gov.hhs.onc.dcdt.io.utils.ToolFileUtils;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolConstraintValidator;
import gov.hhs.onc.dcdt.validation.constraints.io.ToolFileConstraintValidator;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractToolFileConstraintValidator<T extends Annotation> extends AbstractToolConstraintValidator<T, Object> implements
    ToolFileConstraintValidator<T, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext validatorContext) {
        return this.isValidPath(ToolFileUtils.toPath(value), validatorContext);
    }

    protected boolean isValidPath(Path path, ConstraintValidatorContext validatorContext) {
        return true;
    }
}
