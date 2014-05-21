package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.ConstraintComposition;

@Constraint(validatedBy = {})
@ConstraintComposition
@Documented
@Inherited
@NotNull
@Pattern(regexp = ToolMailAddressUtils.PATTERN_STR_MAIL_ADDR_DIRECT)
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface DirectAddress {
    /**
     * Similar to the Hibernate Validator {@link org.hibernate.validator.constraints.Email Email} constraint, but rejects the parts of the underlying
     * specifications that are commonly not implemented/supported (i.e. characters in the local and/or domain parts that are not a word, hyphen, or period).
     */
    @Constraint(validatedBy = {})
    @ConstraintComposition
    @Documented
    @Inherited
    @NotNull
    @Pattern(regexp = ToolMailAddressUtils.PATTERN_STR_MAIL_ADDR)
    @ReportAsSingleViolation
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    @interface MailAddress {
        @Documented
        @Inherited
        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
        public @interface List {
            MailAddress[] value();
        }

        String message() default "{dcdt.mail.validation.constraints.DirectAddress.MailAddress.msg}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        DirectAddress[] value();
    }

    String message() default "{dcdt.mail.validation.constraints.DirectAddress.msg} " + ToolMailAddressUtils.PATTERN_STR_MAIL_ADDR_DIRECT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
