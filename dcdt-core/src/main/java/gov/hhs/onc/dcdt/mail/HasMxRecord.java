package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.mail.impl.HasMxRecordConstraintValidator;
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

@Constraint(validatedBy = { HasMxRecordConstraintValidator.class })
@ConstraintComposition
@Documented
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface HasMxRecord {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        HasMxRecord[] value();
    }
    
    String message() default "{dcdt.mail.validation.constraints.HasMxRecord.msg}";

    String messageLookup() default "{dcdt.mail.validation.constraints.HasMxRecord.lookup.msg}";
    
    String messageLookupTargets() default "{dcdt.mail.validation.constraints.HasMxRecord.lookup.targets.msg}";
    
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
