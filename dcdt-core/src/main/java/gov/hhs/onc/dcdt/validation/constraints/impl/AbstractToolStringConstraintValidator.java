package gov.hhs.onc.dcdt.validation.constraints.impl;

import java.lang.annotation.Annotation;

public abstract class AbstractToolStringConstraintValidator<T extends Annotation> extends AbstractToolConstraintValidator<T, String> {
    public AbstractToolStringConstraintValidator() {
        super(String.class);
    }
}
