package gov.hhs.onc.dcdt.validation.constraints.io;

import gov.hhs.onc.dcdt.validation.constraints.io.impl.FileExistsConstraintValidator;
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

@Constraint(validatedBy = { FileExistsConstraintValidator.class })
@ConstraintComposition(CompositionType.AND)
@Documented
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
public @interface FileExists {
    @ConstraintComposition(CompositionType.OR)
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
    public @interface List {
        FileExists[] value();
    }

    boolean value() default true;

    boolean followLinks() default true;

    String message() default "{dcdt.validation.constraints.io.FileExists.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
