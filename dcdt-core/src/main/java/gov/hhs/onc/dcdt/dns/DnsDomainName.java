package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
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
@Pattern(regexp = ToolDnsNameUtils.PATTERN_STR_DNS_NAME_REL)
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface DnsDomainName {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        DnsDomainName[] value();
    }

    String message() default "{dcdt.dns.validation.constraints.DnsDomainName.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
