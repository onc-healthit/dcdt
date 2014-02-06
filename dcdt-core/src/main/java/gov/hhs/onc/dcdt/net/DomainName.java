package gov.hhs.onc.dcdt.net;

import gov.hhs.onc.dcdt.validation.constraints.NotBlank;
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
import org.hibernate.validator.constraints.Length;

@Constraint(validatedBy = {})
@ConstraintComposition
@Documented
@Inherited
@Length(min = 1, max = 253)
@NotBlank
@Pattern(regexp = "^[\\w\\-\\.]+$")
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface DomainName {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        DomainName[] value();
    }

    String message() default "{dcdt.net.validation.constraints.DomainName.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
