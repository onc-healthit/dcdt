package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.dns.DnsDomainName;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Email;

@Constraint(validatedBy = {})
@ConstraintComposition(CompositionType.OR)
@DnsDomainName
@Documented
@Email
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface DirectAddress {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        DirectAddress[] value();
    }

    String message() default "{dcdt.mail.validation.constraints.DirectAddress.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
