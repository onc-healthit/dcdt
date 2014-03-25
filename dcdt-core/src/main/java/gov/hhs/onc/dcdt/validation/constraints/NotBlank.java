package gov.hhs.onc.dcdt.validation.constraints;

import gov.hhs.onc.dcdt.validation.constraints.impl.NotBlankConstraintValidator;
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

/**
 * @see org.hibernate.validator.constraints.NotBlank
 */
@Constraint(validatedBy = { NotBlankConstraintValidator.class })
@ConstraintComposition
@Documented
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
public @interface NotBlank {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
    public @interface List {
        NotBlank[] value();
    }

    String message() default "{dcdt.validation.constraints.NotBlank.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
