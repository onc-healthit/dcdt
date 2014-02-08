package gov.hhs.onc.dcdt.beans;


import gov.hhs.onc.dcdt.beans.impl.NamedBeanNameConstraintValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import org.hibernate.validator.constraints.ConstraintComposition;

@Constraint(validatedBy = { NamedBeanNameConstraintValidator.class })
@ConstraintComposition
@Documented
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface NamedBeanName {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        NamedBeanName[] value();
    }

    Class<? extends ToolNamedBean> value();
    
    String message() default "{dcdt.beans.validation.constraints.NamedBeanName.msg}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
