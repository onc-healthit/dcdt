package gov.hhs.onc.dcdt.validation.constraints;

import gov.hhs.onc.dcdt.validation.constraints.impl.DomainConstraintValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.ConstraintComposition;

@Constraint(validatedBy = { DomainConstraintValidator.class })
@ConstraintComposition
@Documented
@Inherited
@NotBlank
@Pattern(regexp = "^[\\w\\-\\.]+$")
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
public @interface Domain {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
    public @interface List {
        Domain[] value();
    }

    String message() default "{dcdt.validation.constraints.Domain.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
