package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoSubjectDnConstraintValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.ConstraintComposition;

@Constraint(validatedBy = { CertificateInfoSubjectDnConstraintValidator.class })
@ConstraintComposition
@Documented
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
public @interface CertificateInfoSubjectDn {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
    public @interface List {
        CertificateInfoSubjectDn[] value();
    }

    String message() default StringUtils.EMPTY;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
