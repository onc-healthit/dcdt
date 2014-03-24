package gov.hhs.onc.dcdt.validation.constraints;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;

public interface ToolConstraintValidator<T extends Annotation, U> extends ConstraintValidator<T, U>, ToolBean {
}
