package gov.hhs.onc.dcdt.validation.constraints.impl;

import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.validation.constraints.ToolParameterConstraintValidator;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ClassUtils;

public abstract class AbstractToolParameterConstraintValidator<T extends Annotation> extends AbstractToolConstraintValidator<T, Object[]> implements
    ToolParameterConstraintValidator<T> {
    protected final static String ANNO_ATTR_NAME_PARAM_CLASSES = "parameterClasses";

    protected Class<?>[] annoParamClasses;

    public AbstractToolParameterConstraintValidator() {
        super(Object[].class);
    }

    @Override
    public void initialize(T anno) {
        super.initialize(anno);

        this.annoParamClasses = ToolArrayUtils.emptyToNull(ToolAnnotationUtils.getValue(this.anno, Class[].class, ANNO_ATTR_NAME_PARAM_CLASSES));
    }

    @Override
    protected boolean canValidate(Object[] params, ConstraintValidatorContext validatorContext) {
        return (super.canValidate(params, validatorContext) && ((this.annoParamClasses == null) || ToolClassUtils.isAssignable(ClassUtils.toClass(params),
            this.annoParamClasses)));
    }
}
